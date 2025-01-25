package com.example.servingwebcontent.service.quiz.impl;

import com.example.servingwebcontent.exceptions.AccessDeniedException;
import com.example.servingwebcontent.model.quiz.result.QuizTaskCompleteCommand;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import com.example.servingwebcontent.persistence.QuizPersistence;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizInvokeServiceImpl implements QuizInvokeService {

	private final QuizPersistence quizPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createNewQuizResult(Long userId, Long quizId) {
		quizPersistence.createNewQuizResult(userId, quizId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizTaskResultWithTaskType> getQuizTaskListByQuizResultId(Long userId, Long quizResultId) {
		if (quizPersistence.notExistsQuizByUserId(userId, quizResultId)) {
			throw AccessDeniedException.quizByUserId(userId, quizResultId);
		}
		return quizPersistence.getQuizTaskResultByQuizResultId(quizResultId);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveTaskResult(Long userId, QuizTaskCompleteCommand command) {
		if (quizPersistence.notExistsQuizByUserIdAndTask(userId, command.getQuizTaskResultId())) {
			throw AccessDeniedException.quizTaskByUserId(userId, command.getQuizTaskResultId());
		}
		quizPersistence.saveTaskResult(command);
	}

}
