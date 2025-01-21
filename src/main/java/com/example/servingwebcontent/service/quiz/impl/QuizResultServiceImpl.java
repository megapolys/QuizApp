package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionByTask;
import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.result.*;
import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.persistence.DecisionPersistence;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.property.FiveVariantProperty;
import com.example.servingwebcontent.property.YesOrNoProperty;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class QuizResultServiceImpl implements QuizResultService {

	private final QuizPersistence quizPersistence;
	private final DecisionPersistence decisionPersistence;
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
	public QuizFullResultDto getQuizResultById(Long quizResultId) {
		Quiz quiz = quizPersistence.getQuizByQuizResultId(quizResultId);
		List<QuizTaskResultWithTaskType> taskResultList = quizPersistence.getQuizTaskResultByQuizResultId(quizResultId);
		Map<Long, List<Decision>> decisionsByTaskId = decisionPersistence.findAllDecisionsByQuizId(quiz.getId()).stream()
			.collect(Collectors.groupingBy(DecisionByTask::getTaskId, Collectors.mapping(decisionByTask -> Decision.builder()
				.id(decisionByTask.getId())
				.name(decisionByTask.getName())
				.description(decisionByTask.getDescription())
				.build(), toList())));
		List<QuizTaskResultCalculated> results = new ArrayList<>();
		final Map<Decision, Float> decisionWeightMap = new LinkedHashMap<>();
		final Map<Decision, Integer> decisionCountMap = new LinkedHashMap<>();
		for (QuizTaskResultWithTaskType taskResult : taskResultList) {
			final float weight = taskResult.getAltScore() == null ? getWeight(taskResult) : taskResult.getAltScore();
			if (decisionsByTaskId.containsKey(taskResult.getTaskId())) {
				for (Decision decision : decisionsByTaskId.get(taskResult.getTaskId())) {
					decisionWeightMap.compute(decision, (dec, localWeight) -> localWeight == null ? weight : localWeight + weight);
					decisionCountMap.compute(decision, (dec, count) -> count == null ? 1 : count + 1);
				}
			}
			results.add(QuizTaskResultCalculated.builder()
				.id(taskResult.getId())
				.position(taskResult.getPosition())
				.text(taskResult.getText())
				.questionText(taskResult.getFiveVariantTask() == null ? taskResult.getYesOrNoTask().getQuestionText() : taskResult.getFiveVariantTask().getQuestionText())
				.resultScore(weight)
				.altScore(taskResult.getAltScore())
				.build());
		}
		results.sort(Comparator.comparing(QuizTaskResultCalculated::getPosition));
		List<DecisionCalculated> decisions = decisionWeightMap.entrySet().stream()
			.map(entry -> DecisionCalculated.builder()
				.decision(entry.getKey())
				.score(entry.getValue())
				.count(decisionCountMap.get(entry.getKey()))
				.build())
			.sorted(Comparator.comparing(DecisionCalculated::getScore, Comparator.reverseOrder()))
			.toList();
		return QuizFullResultDto.builder()
			.quizName(quiz.getName())
			.decisions(decisions)
			.results(results)
			.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizResultById(Long quizResultId) {
		quizPersistence.deleteQuizResultById(quizResultId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QuizTaskResultWithTaskType getQuizTaskResultById(Long quizTaskResultId) {
		QuizTaskResultWithTaskType quizTaskResult = quizPersistence.getQuizTaskResultById(quizTaskResultId);
		return quizTaskResult.toBuilder()
			.score(getWeight(quizTaskResult))
			.build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuizTaskResult(QuizTaskResultUpdateCommandDto command) {
		quizPersistence.updateQuizTaskResult(command);
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
