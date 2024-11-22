package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.quiz.*;

import java.util.List;

public interface QuizPersistence {

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
	 * Получение теста по короткому наименованию
	 *
	 * @return Тест
	 */
	Quiz findByShortName(String shortName);

	/**
	 * Добавить новый тест
	 *
	 * @param quiz - Тест
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

	/**
	 * Получить вопрос целиком
	 *
	 * @param taskId идентификатор вопроса
	 *
	 * @return вопрос
	 */
	QuizTaskFull getQuizTaskById(Long taskId);
}
