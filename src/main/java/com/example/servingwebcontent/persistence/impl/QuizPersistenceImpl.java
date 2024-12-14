package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.QuizTaskFullEntityToQuizTaskFullConverter;
import com.example.servingwebcontent.exceptions.quiz.QuizNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskNotFoundException;
import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.quiz.*;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.quiz.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizRepository quizRepository;
	private final QuizResultRepository quizResultRepository;
	private final QuizTaskRepository quizTaskRepository;
	private final QuizTaskResultRepository quizTaskResultRepository;
	private final DecisionRepository decisionRepository;
	private final FiveVariantRepository fiveVariantRepository;
	private final YesOrNoRepository yesOrNoRepository;
	private final QuizTaskDecisionsRepository quizTaskDecisionsRepository;

	private final ConversionService conversionService;
	private final QuizTaskFullEntityToQuizTaskFullConverter quizTaskFullConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizRepository.getQuizListOrderedByShortName().stream()
			.map(quizEntity -> conversionService.convert(quizEntity, QuizWithTaskSize.class))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz getQuiz(Long id) {
		return quizRepository.findById(id)
			.map(quizEntity -> conversionService.convert(quizEntity, Quiz.class))
			.orElseThrow(() -> QuizNotFoundException.byId(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizTask> getQuizTaskList(Long quizId) {
		return quizTaskRepository.findAllFullByQuizId(quizId).stream()
			.map(quizTaskEntity -> {
				String text;
				if (quizTaskEntity.getFiveVariantTaskEntity() != null) {
					text = quizTaskEntity.getFiveVariantTaskEntity().getQuestionText();
				} else {
					text = quizTaskEntity.getYesOrNoTaskEntity().getQuestionText();
				}
				return QuizTask.builder()
					.id(quizTaskEntity.getId())
					.position(quizTaskEntity.getPosition())
					.text(text)
					.decisionsCount(quizTaskEntity.getCountDecisions())
					.build();
			})
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz findByShortName(String shortName) {
		return quizRepository.findByShortName(shortName)
			.map(quizEntity -> conversionService.convert(quizEntity, Quiz.class))
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addQuiz(QuizCreateCommandDto quiz) {
		quizRepository.save(QuizEntity.createNew(
			quiz.getName(),
			quiz.getShortName()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuiz(QuizUpdateCommandDto quiz) {
		quizRepository.save(QuizEntity.buildExisting(
			quiz.getId(),
			quiz.getName(),
			quiz.getShortName()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizById(Long id) {
		quizRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizResultByQuizId(Long id) {
		quizResultRepository.deleteAllByQuizId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public QuizTaskFull getQuizTaskFullById(Long taskId) {
		return quizTaskRepository.findFullByTaskId(taskId)
			.map(quizTaskFullEntity -> {
				List<Long> allIdsByTaskId = decisionRepository.findAllIdsByTaskId(taskId);
				return quizTaskFullConverter.convert(quizTaskFullEntity, allIdsByTaskId);
			})
			.orElseThrow(() -> QuizTaskNotFoundException.byId(taskId));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTaskResultByTaskId(Long taskId) {
		quizTaskResultRepository.deleteByTaskId(taskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteTaskById(Long taskId) {
		QuizTaskEntity quizTaskEntity = quizTaskRepository.findById(taskId)
			.orElseThrow(() -> QuizTaskNotFoundException.byId(taskId));
		quizTaskDecisionsRepository.deleteAllByQuizTaskId(taskId);
		quizTaskRepository.deleteById(taskId);
		if (quizTaskEntity.getQuizTaskFiveVariantId() != null) {
			fiveVariantRepository.deleteById(quizTaskEntity.getQuizTaskFiveVariantId());
		}
		if (quizTaskEntity.getQuizTaskYesOrNoId() != null) {
			yesOrNoRepository.deleteById(quizTaskEntity.getQuizTaskYesOrNoId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rePositionTasksByQuizId(Long quizId) {
		List<QuizTaskEntity> taskList = quizTaskRepository.findAllByQuizId(quizId);
		taskList.sort(Comparator.comparing(QuizTaskEntity::getPosition));
		int position = 1;
		for (QuizTaskEntity quizTaskEntity : taskList) {
			quizTaskEntity.setPosition(position++);
		}
		quizTaskRepository.saveAll(taskList);
	}
}
