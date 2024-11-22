package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.exceptions.quiz.QuizNotFoundException;
import com.example.servingwebcontent.exceptions.quiz.QuizTaskNotFoundException;
import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.quiz.*;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizRepository quizRepository;
	private final QuizTaskRepository quizTaskRepository;

	private final ConversionService conversionService;

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
		return quizTaskRepository.findAllByQuizId(quizId).stream()
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
	public QuizTaskFull getQuizTaskById(Long taskId) {
		QuizTaskEntity quizTaskEntity = quizTaskRepository.findById(taskId)
			.orElseThrow(() -> QuizTaskNotFoundException.byId(taskId));

	}
}
