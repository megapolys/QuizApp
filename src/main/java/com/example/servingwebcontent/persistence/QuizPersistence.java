package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;

import java.util.List;

public interface QuizPersistence {

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов
	 */
	List<QuizWithTaskSize> getQuizList();
}
