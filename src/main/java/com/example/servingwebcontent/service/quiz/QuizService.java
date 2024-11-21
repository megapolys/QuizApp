package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.*;

import java.util.List;

public interface QuizService {

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов
	 */
	List<QuizWithTaskSize> getQuizList();

	/**
	 * Получение теста по идентификатору
	 *
	 * @param id - Идентификатор теста
	 *
	 * @return Тест
	 */
	Quiz getQuiz(Long id);

	/**
	 * Получить список вопросов
	 *
	 * @param quizId идентификатор теста
	 *
	 * @return список вопросов
	 */
	List<QuizTask> getQuizTaskList(Long quizId);

	/**
	 * Создание нового теста
	 *
	 * @param quiz - Новый тест
	 */
	void addQuiz(QuizCreateCommandDto quiz);

	/**
	 * Изменение данных теста
	 *
	 * @param quiz - Тест
	 */
	void updateQuiz(QuizUpdateCommandDto quiz);

	/**
	 * Удаление теста
	 *
	 * @param id - Идентификатор теста
	 */
	void deleteQuizById(Long id);
}
