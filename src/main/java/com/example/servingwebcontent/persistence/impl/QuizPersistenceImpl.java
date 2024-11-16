package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.QuizCreateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.custom.QuizCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizCustomRepository quizCustomRepository;
	private final QuizRepository quizRepository;
	private final ConversionService conversionService;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizCustomRepository.getQuizListOrderedByShortName().stream()
				.map(quizEntity -> conversionService.convert(quizEntity, QuizWithTaskSize.class))
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
		quizRepository.save(QuizEntity.createNew(quiz.getName(), quiz.getShortName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteQuizById(Long id) {
		quizRepository.deleteById(id);
	}
}
