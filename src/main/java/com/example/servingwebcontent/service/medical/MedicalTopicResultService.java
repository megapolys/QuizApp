package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.medical.MedicalTask;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.model.medical.result.MedicalTaskResult;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.repositories.medical.MedicalTaskResultRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalTopicResultService {

    @Value("${default.medical.leftLeft}")
    private float leftLeft;
    @Value("${default.medical.leftMid}")
    private float leftMid;
    @Value("${default.medical.rightMid}")
    private float rightMid;
    @Value("${default.medical.rightRight}")
    private float rightRight;

    @Value("${default.ratio.yellow}")
    private float yellowRatio;
    @Value("${default.ratio.red}")
    private float redRatio;

    private final MedicalTopicInvokeService topicInvokeService;
    private final MedicalTopicResultRepository topicResultRepository;
    private final MedicalTaskResultRepository taskResultRepository;

    public MedicalTopicResultService(MedicalTopicInvokeService topicInvokeService, MedicalTopicResultRepository topicResultRepository, MedicalTaskResultRepository taskResultRepository) {
        this.topicInvokeService = topicInvokeService;
        this.topicResultRepository = topicResultRepository;
        this.taskResultRepository = taskResultRepository;
    }

    public List<TopicResultBean> getTopics(User user) {
        final Set<MedicalTopicResult> results = user.getMedicalResults();
        return results.stream().collect(Collectors.groupingBy(MedicalTopicResult::getMedicalTopic)).entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getName()))
                .map(entry -> new TopicResultBean(entry.getKey(), entry.getValue().stream()
                        .sorted(Comparator.comparing(MedicalTopicResult::getCompleteDate, Comparator.nullsFirst(Comparator.reverseOrder())))
                        .map(this::getResult)
                        .toList()
                )).toList();
    }

    public ResultBean getResult(Long topicResultId) {
//        final MedicalTopicResult result = topicResultRepository.findById(topicResultId).orElseThrow(); // нужно для актуализации данных из бд
//        return getResult(result);
        return null;
    }

    private ResultBean getResult(MedicalTopicResult result) {
		final Map<Decision, Float> decisionBeans = new LinkedHashMap<>();
		final Map<Decision, Integer> decisionsCount = new LinkedHashMap<>();
		float weightSum = 0;
		int filledCount = 0;
		final String progress = topicInvokeService.getProgress(result);
		if (result.getCompleteDate() == null) {
			return new ResultBean(result, null, null, 0, false, false, progress);
		}
		final List<TaskResultBean> tasks = new ArrayList<>();
		for (MedicalTaskResult taskResult : result.getResults()) {
			final MedicalTask task = taskResult.getMedicalTask();
			if (taskResult.getValue() == null) {
                continue;
            }

			Set<Decision> decisions = Set.of();
			boolean left = false;
            boolean right = false;
            if (taskResult.getValue() <= task.getLeftMid()) {
                decisions = task.getLeftDecisions();
                left = true;
            }
            if (taskResult.getValue() >= task.getRightMid()) {
                decisions = task.getRightDecisions();
                right = true;
            }

            final float weight;
            if (taskResult.getAltScore() != null) {
                weight = taskResult.getAltScore();
            } else if (left) {
                if (taskResult.getValue() <= task.getLeftLeft()) {
                    weight = leftLeft;
                } else {
                    weight = leftMid;
                }
            } else if (right) {
                if (taskResult.getValue() >= task.getRightRight()) {
                    weight = rightRight;
                } else {
                    weight = rightMid;
                }
            } else {
                weight = 0;
            }

			for (Decision decision : decisions) {
				decisionBeans.compute(decision, (dec, localWeight) -> localWeight == null ? weight : localWeight + weight);
				decisionsCount.compute(decision, (dec, count) -> count == null ? 1 : count + 1);
			}
			tasks.add(new TaskResultBean(taskResult, weight, getAnalyse(taskResult), decisions.stream().sorted(Comparator.comparing(Decision::getName)).toList()));
			weightSum += weight;
            filledCount++;
        }
        final float score = weightSum / filledCount;
        final List<DecisionBean> decisionsList = decisionBeans.entrySet().stream()
                .map(entry -> new DecisionBean(entry.getKey(), entry.getValue(), entry.getValue() / decisionsCount.get(entry.getKey()), decisionsCount.get(entry.getKey())))
                .sorted(Comparator.comparing(DecisionBean::score).reversed()
                        .thenComparing(bean -> bean.decision().getName()))
                .toList();
        result.setResults(result.getResults().stream()
                .sorted(Comparator.comparing(taskResult -> taskResult.getMedicalTask().getName()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        tasks.sort(Comparator.comparing(t -> t.taskResult.getMedicalTask().getName()));
        return new ResultBean(result, tasks, decisionsList, score, score >= yellowRatio, score >= redRatio, progress);
    }

    public void deleteResult(Long topicResultId) {
        topicResultRepository.deleteById(topicResultId);
    }

    public void updateTaskResult(MedicalTaskResult taskResult) {
//        taskResultRepository.save(taskResult);
    }

    public AnalyseForm getAnalyse(MedicalTaskResult result) {
        final float value = result.getValue();
        final MedicalTask task = result.getMedicalTask();
        final List<Float> values = new ArrayList<>();
        final float gap = task.getRightRight() - task.getLeftLeft();
        final float percent = 0.1f;
        final float min = Math.min(value, task.getLeftLeft()) - gap * percent;
        final float max = Math.max(value, task.getRightRight()) + gap * percent;
        final float len = max - min;
        values.add((task.getLeftLeft() - min) / len * 100);
        values.add((task.getLeftMid() - task.getLeftLeft()) / len * 100);
        values.add((task.getRightMid() - task.getLeftMid()) / len * 100);
        values.add((task.getRightRight() - task.getRightMid()) / len * 100);
        values.add((max - task.getRightRight()) / len * 100);
        final float marker = (value - min) / len * 100;
        return new AnalyseForm(values, marker);
    }

	public record TopicResultBean(MedicalTopicWithTaskSize topic, List<ResultBean> results) {

	}

    public record ResultBean(MedicalTopicResult topicResult, List<TaskResultBean> tasks, List<DecisionBean> decisions,
                             float score, boolean yellow, boolean red, String progress) {
    }

	public record TaskResultBean(MedicalTaskResult taskResult, Float resultScore, AnalyseForm analyseForm,
								 List<Decision> decisions) {
	}

    public record AnalyseForm(List<Float> values, float marker){}

	public record DecisionBean(Decision decision, float score, float altScore, int count) {
	}
}
