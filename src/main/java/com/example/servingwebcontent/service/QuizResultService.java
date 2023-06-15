package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizResultService {

    @Value("${default.fiveVariant.firstWeight}")
    private float firstWeight;
    @Value("${default.fiveVariant.secondWeight}")
    private float secondWeight;
    @Value("${default.fiveVariant.thirdWeight}")
    private float thirdWeight;
    @Value("${default.fiveVariant.fourthWeight}")
    private float fourthWeight;
    @Value("${default.fiveVariant.fifthWeight}")
    private float fifthWeight;

    @Value("${default.yesOrNo.noWeight}")
    private float noWeight;
    @Value("${default.yesOrNo.yesWeight}")
    private float yesWeight;

    @Value("${default.ratio.yellow}")
    private float yellowRatio;
    @Value("${default.ratio.red}")
    private float redRatio;

    private final UserRepository userRepository;
    private final QuizInvokeService quizInvokeService;

    public QuizResultService(UserRepository userRepository, QuizInvokeService quizInvokeService) {
        this.userRepository = userRepository;
        this.quizInvokeService = quizInvokeService;
    }

    public List<ResultBean> getResults(Long userId) {
        final Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException(String.format("User by userId(%s) is not found", userId));
        }
        final User user = optionalUser.get();
        final List<ResultBean> resultBeans = new ArrayList<>(user.getResults().stream().map(this::getResult).toList());
        resultBeans.sort(Comparator.comparing(bean -> bean.quizResult.getCompleteDate(), Comparator.nullsLast(Comparator.reverseOrder())));
        return resultBeans;
    }

    public ResultBean getResult(QuizResult result) {
        final Map<QuizDecision, Float> decisionBeans = new LinkedHashMap<>();
        float weightSum = 0;
        final String progress = quizInvokeService.getProgress(result);
        if (!result.isComplete()) {
            return new ResultBean(result, null, 0, false, false, progress);
        }
        for (QuizTaskResult taskResult : result.getTaskList()) {
            final QuizTask task = taskResult.getTask();
            final Set<QuizDecision> decisions = task.getDecisions();
            final float weight = taskResult.getAltScore() == null ? getWeight(taskResult) : taskResult.getAltScore();
            for (QuizDecision decision : decisions) {
                decisionBeans.compute(decision, (dec, localWeight) -> localWeight == null ? weight : localWeight + weight);
            }
            taskResult.setAltScore(weight); //сделано для отображения балла, не сохраняется в бд
            weightSum += taskResult.getAltScore();
        }
        final float score = weightSum / result.getTaskList().size();
        final List<DecisionBean> decisionsList = decisionBeans.entrySet().stream()
                .map(entry -> new DecisionBean(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(bean -> bean.score, Comparator.reverseOrder()))
                .toList();
        result.setTaskList(result.getTaskList().stream()
                .sorted(Comparator.comparing(taskResult -> taskResult.getTask().getPosition()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        return new ResultBean(result, decisionsList,
                score, score > yellowRatio, score > redRatio, progress);
    }

    public float getWeight(QuizTaskResult taskResult) {
        float weight = 0;
        final QuizTask task = taskResult.getTask();
        final FiveVariantTask fiveVariantTask = task.getFiveVariantTask();
        if (fiveVariantTask != null) {
            weight = switch (taskResult.getVariant()) {
                case "1" -> withDefault(fiveVariantTask.getFifthWeight(), firstWeight);
                case "2" -> withDefault(fiveVariantTask.getSecondWeight(), secondWeight);
                case "3" -> withDefault(fiveVariantTask.getThirdWeight(), thirdWeight);
                case "4" -> withDefault(fiveVariantTask.getFourthWeight(), fourthWeight);
                case "5" -> withDefault(fiveVariantTask.getFifthWeight(), fifthWeight);
                default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
            };
        }
        final YesOrNoTask yesOrNoTask = task.getYesOrNoTask();
        if (yesOrNoTask != null) {
            weight = switch (taskResult.getVariant()) {
                case "1" -> withDefault(yesOrNoTask.getYesWeight(), yesWeight);
                case "2" -> withDefault(yesOrNoTask.getNoWeight(), noWeight);
                default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
            };
        }
        return weight;
    }

    private float withDefault(Float weight, float defaultWeight) {
        return weight == null ? defaultWeight : weight;
    }

    public record ResultBean(QuizResult quizResult, List<DecisionBean> decisions,
                             float score, boolean yellow, boolean red, String progress) {
    }

    public record DecisionBean(QuizDecision decision, float score) {
    }

}
