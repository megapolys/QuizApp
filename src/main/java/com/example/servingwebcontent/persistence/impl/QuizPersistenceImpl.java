package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.QuizTaskFullEntityToQuizTaskFullConverter;
import com.example.servingwebcontent.exceptions.quiz.QuizNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskNotFoundException;
import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.quiz.*;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizRepository quizRepository;
	private final QuizTaskRepository quizTaskRepository;
	private final QuizTaskResultRepository quizTaskResultRepository;
	private final DecisionRepository decisionRepository;

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
	public void deleteTaskById(Long taskId) {
		quizTaskRepository.deleteById(taskId);
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
