package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.exceptions.quiz.QuizCreateException;
import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.QuizCreateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.service.quiz.QuizService;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

	private final QuizPersistence quizPersistence;
	private final QuizTaskService quizTaskService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSize> getQuizList() {
		return quizPersistence.getQuizList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Quiz getQuiz(Long id) {
		return quizPersistence.getQuiz(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addQuiz(QuizCreateCommandDto quiz) {
		Quiz byShortName = quizPersistence.findByShortName(quiz.getShortName());
		if (byShortName != null) {
			throw QuizCreateException.alreadyExistsByShortName(quiz.getShortName());
		}
		quizPersistence.addQuiz(quiz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateQuiz(QuizUpdateCommandDto quiz) {
		Quiz byShortName = quizPersistence.findByShortName(quiz.getShortName());
		if (byShortName != null && !quiz.getId().equals(byShortName.getId())) {
			throw QuizCreateException.alreadyExistsByShortName(quiz.getShortName());
		}
		quizPersistence.updateQuiz(quiz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteQuizById(Long id) {
		quizTaskService.deleteAllQuizTask(id);
		quizPersistence.deleteQuizResultByQuizId(id);
		quizPersistence.deleteQuizById(id);
	}

}
