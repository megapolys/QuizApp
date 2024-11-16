package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.QuizCreateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;

import java.util.List;

public interface QuizService {

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов
	 */
	List<QuizWithTaskSize> getQuizList();

	/**
	 * Создание нового теста
	 *
	 * @param quiz - Новый тест
	 */
	void addQuiz(QuizCreateCommandDto quiz);

	/**
	 * Удаление теста
	 *
	 * @param id - Идентификатор теста
	 */
	void deleteQuizById(Long id);
}
