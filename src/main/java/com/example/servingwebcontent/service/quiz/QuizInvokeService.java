package com.example.servingwebcontent.service.quiz;

public interface QuizInvokeService {

	/**
	 * Назначение пользователю теста на выполнение (создание нового результата теста)
	 *
	 * @param userId идентификатор пользователя, которому назначается тест
	 * @param quizId идентификатор теста
	 */
	void createNewQuizResult(Long userId, Long quizId);
}
