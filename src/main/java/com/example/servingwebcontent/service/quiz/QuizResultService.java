package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.repositories.quiz.QuizResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {

    @Value("${default.ratio.yellow}")
    private float yellowRatio;
    @Value("${default.ratio.red}")
    private float redRatio;

	private final UserRepository userRepository;
	private final QuizInvokeService quizInvokeService;
	public final QuizResultRepository quizResultRepository;

	public QuizResultService(UserRepository userRepository, QuizInvokeService quizInvokeService, QuizResultRepository quizResultRepository) {
		this.userRepository = userRepository;
		this.quizInvokeService = quizInvokeService;
		this.quizResultRepository = quizResultRepository;
	}

//    public List<QuizResultBean> getResults(User user) {
//        final Set<QuizResult> results = user.getResults();
//        return results.stream().collect(Collectors.groupingBy(QuizResult::getQuiz)).entrySet().stream()
//                .sorted(Comparator.comparing(entry -> entry.getKey().getShortName()))
//                .map(entry -> new QuizResultBean(entry.getKey(), entry.getValue().stream()
//                        .sorted(Comparator.comparing(QuizResult::getCompleteDate, Comparator.nullsFirst(Comparator.reverseOrder())))
//                        .map(this::getResult)
//                        .toList()
//                )).toList();
//    }
//
//    public ResultBean getResult(QuizResult result) {
//		final Map<Decision, Float> decisionBeans = new LinkedHashMap<>();
//		final Map<Decision, Integer> decisionsCount = new LinkedHashMap<>();
//		float weightSum = 0;
//		final String progress = quizInvokeService.getProgress(result);
//		if (!result.isComplete()) {
//			return new ResultBean(result, null, 0, false, false, progress);
//		}
//		for (QuizTaskResult taskResult : result.getTaskList()) {
//			final QuizTask task = taskResult.getTask();
//			final Set<Decision> decisions = task.getDecisions();
//			final float weight = taskResult.getAltScore() == null ? getWeight(taskResult) : taskResult.getAltScore();
//			for (Decision decision : decisions) {
//				decisionBeans.compute(decision, (dec, localWeight) -> localWeight == null ? weight : localWeight + weight);
//				decisionsCount.compute(decision, (dec, count) -> count == null ? 1 : count + 1);
//			}
//            taskResult.setResultScore(weight); //сделано для отображения балла, не сохраняется в бд
//            weightSum += weight;
//        }
//        final float score = weightSum / result.getTaskList().size();
//        final List<DecisionBean> decisionsList = decisionBeans.entrySet().stream()
//                .map(entry -> new DecisionBean(entry.getKey(), entry.getValue(), entry.getValue() / decisionsCount.get(entry.getKey()), decisionsCount.get(entry.getKey())))
//                .sorted(Comparator.comparing(bean -> bean.score, Comparator.reverseOrder()))
//                .toList();
//        result.setTaskList(result.getTaskList().stream()
//                .sorted(Comparator.comparing(taskResult -> taskResult.getTask().getPosition()))
//                .collect(Collectors.toCollection(LinkedHashSet::new)));
//        return new ResultBean(result, decisionsList, score, score > yellowRatio, score > redRatio, progress);
//    }
//
//    public float getWeight(QuizTaskResult taskResult) {
//        float weight = 0;
//        final QuizTask task = taskResult.getTask();
//        final FiveVariantTask fiveVariantTask = task.getFiveVariantTask();
//        if (fiveVariantTask != null) {
//            weight = switch (taskResult.getVariant()) {
//                case "1" -> withDefault(fiveVariantTask.getFifthWeight(), firstWeight);
//                case "2" -> withDefault(fiveVariantTask.getSecondWeight(), secondWeight);
//                case "3" -> withDefault(fiveVariantTask.getThirdWeight(), thirdWeight);
//                case "4" -> withDefault(fiveVariantTask.getFourthWeight(), fourthWeight);
//                case "5" -> withDefault(fiveVariantTask.getFifthWeight(), fifthWeight);
//                default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
//            };
//        }
//        final YesOrNoTask yesOrNoTask = task.getYesOrNoTask();
//        if (yesOrNoTask != null) {
//            weight = switch (taskResult.getVariant()) {
//                case "1" -> withDefault(yesOrNoTask.getYesWeight(), yesWeight);
//                case "2" -> withDefault(yesOrNoTask.getNoWeight(), noWeight);
//                default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
//            };
//        }
//        return weight;
//    }

	private float withDefault(Float weight, float defaultWeight) {
		return weight == null ? defaultWeight : weight;
	}

	@Transactional
	public void deleteResult(Long quizResultId) {
		quizResultRepository.deleteById(quizResultId);
	}

	public record QuizResultBean(QuizWithTaskSize quiz, List<ResultBean> results) {
	}

    public record ResultBean(QuizResult quizResult, List<DecisionBean> decisions,
                             float score, boolean yellow, boolean red, String progress) {}

	public record DecisionBean(Decision decision, float score, float altScore, int count) {
	}

}
