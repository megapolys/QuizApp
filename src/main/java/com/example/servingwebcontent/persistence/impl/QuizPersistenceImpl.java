package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.QuizWithTaskSizeEntityToQuizConverter;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.repositories.quiz.custom.QuizCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizPersistenceImpl implements QuizPersistence {

	private final QuizCustomRepository quizCustomRepository;
	private final QuizWithTaskSizeEntityToQuizConverter quizWithTaskSizeEntityToQuizConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizCustomRepository.getQuizListOrderedByShortName().stream()
			.map(quizWithTaskSizeEntityToQuizConverter::convert)
			.toList();
	}
}
