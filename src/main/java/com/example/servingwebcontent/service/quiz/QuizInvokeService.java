package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizTaskCompleteCommand;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;

import java.util.List;

public interface QuizInvokeService {

	/**
	 * Назначение пользователю теста на выполнение (создание нового результата теста)
	 *
	 * @param userId идентификатор пользователя, которому назначается тест
	 * @param quizId идентификатор теста
	 */
	void createNewQuizResult(Long userId, Long quizId);

	/**
	 * Получение списка вопросов по тесту
	 *
	 * @param userId       идентификатор пользователя
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return список вопросов
	 */
	List<QuizTaskResultWithTaskType> getQuizTaskListByQuizResultId(Long userId, Long quizResultId);

	/**
	 * Сохранить результат ответа
	 *
	 * @param userId  идентификатор пользователя
	 * @param command команда с результатом ответа
	 */
	void saveTaskResult(Long userId, QuizTaskCompleteCommand command);
}
