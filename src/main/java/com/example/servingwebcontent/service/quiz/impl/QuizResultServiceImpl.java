package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.model.quiz.result.QuizResultCalculated;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import com.example.servingwebcontent.model.quiz.result.QuizWithResults;
import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.property.FiveVariantProperty;
import com.example.servingwebcontent.property.YesOrNoProperty;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizResultServiceImpl implements QuizResultService {

	private final QuizPersistence quizPersistence;
	private final FiveVariantProperty fiveVariantProperty;
	private final YesOrNoProperty yesOrNoProperty;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithResults> getQuizResultListByUserId(Long userId) {
		List<QuizResult> quizResults = quizPersistence.getQuizResultListByUserId(userId);
		Map<Long, Quiz> quizMapById = quizPersistence.getAllQuizzes().stream()
			.collect(Collectors.toMap(Quiz::getId, Function.identity()));
		return quizResults.stream()
			.collect(Collectors.groupingBy(QuizResult::getQuizId))
			.entrySet().stream()
			.map(entry -> QuizWithResults.builder()
				.quiz(quizMapById.get(entry.getKey()))
				.results(entry.getValue().stream()
					.sorted(Comparator.comparing(QuizResult::getCompleteDate, Comparator.nullsFirst(Comparator.reverseOrder())))
					.map(this::getResult)
					.toList()
				)
				.build())
			.sorted(Comparator.comparing((QuizWithResults quizWithResults) -> quizWithResults.getQuiz().getShortName()))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizResultById(Long quizResultId) {
		quizPersistence.deleteQuizResultById(quizResultId);
	}

	public QuizResultCalculated getResult(QuizResult result) {
		List<QuizTaskResultWithTaskType> taskResultList = quizPersistence.getQuizTaskResultByQuizResultId(result.getId());
		int taskCount = taskResultList.size();
		Long countCompleted = taskResultList.stream()
			.filter(QuizTaskResultWithTaskType::isComplete)
			.count();
		if (!result.isComplete()) {
			return QuizResultCalculated.builder()
				.id(result.getId())
				.complete(false)
				.completeDate(result.getCompleteDate())
				.score(0)
				.taskCount(taskCount)
				.countCompleted(countCompleted)
				.build();
		}
		float weightSum = 0;
		for (QuizTaskResultWithTaskType taskResult : taskResultList) {
			final float weight = taskResult.getAltScore() == null ? getWeight(taskResult) : taskResult.getAltScore();
			weightSum += weight;
		}
		final float score = weightSum / taskCount;
		return QuizResultCalculated.builder()
			.id(result.getId())
			.complete(true)
			.completeDate(result.getCompleteDate())
			.score(score)
			.taskCount(taskCount)
			.countCompleted(countCompleted)
			.build();
	}

//	public ResultBean getResult(QuizResult result) {
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
//			taskResult.setResultScore(weight); //сделано для отображения балла, не сохраняется в бд
//			weightSum += weight;
//		}
//		final float score = weightSum / result.getTaskList().size();
//		final List<DecisionBean> decisionsList = decisionBeans.entrySet().stream()
//			.map(entry -> new DecisionBean(entry.getKey(), entry.getValue(), entry.getValue() / decisionsCount.get(entry.getKey()), decisionsCount.get(entry.getKey())))
//			.sorted(Comparator.comparing(bean -> bean.score, Comparator.reverseOrder()))
//			.toList();
//		result.setTaskList(result.getTaskList().stream()
//			.sorted(Comparator.comparing(taskResult -> taskResult.getTask().getPosition()))
//			.collect(Collectors.toCollection(LinkedHashSet::new)));
//		return new ResultBean(result, decisionsList, score, score > yellowRatio, score > redRatio, progress);
//	}

	public float getWeight(QuizTaskResultWithTaskType taskResult) {
		float weight = 0;
		FiveVariantTask fiveVariantTask = taskResult.getFiveVariantTask();
		if (fiveVariantTask != null) {
			weight = switch (taskResult.getVariant()) {
				case "1" -> withDefault(fiveVariantTask.getFifthWeight(), fiveVariantProperty.getFirstWeight());
				case "2" -> withDefault(fiveVariantTask.getSecondWeight(), fiveVariantProperty.getSecondWeight());
				case "3" -> withDefault(fiveVariantTask.getThirdWeight(), fiveVariantProperty.getThirdWeight());
				case "4" -> withDefault(fiveVariantTask.getFourthWeight(), fiveVariantProperty.getFourthWeight());
				case "5" -> withDefault(fiveVariantTask.getFifthWeight(), fiveVariantProperty.getFifthWeight());
				default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
			};
		}
		YesOrNoTask yesOrNoTask = taskResult.getYesOrNoTask();
		if (yesOrNoTask != null) {
			weight = switch (taskResult.getVariant()) {
				case "1" -> withDefault(yesOrNoTask.getYesWeight(), yesOrNoProperty.getYesWeight());
				case "2" -> withDefault(yesOrNoTask.getNoWeight(), yesOrNoProperty.getNoWeight());
				default -> throw new IllegalArgumentException("Unknown value of taskResult.variant!");
			};
		}
		return weight;
	}

	private float withDefault(Float weight, float defaultWeight) {
		return weight == null ? defaultWeight : weight;
	}
}
