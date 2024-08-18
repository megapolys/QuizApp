package com.example.servingwebcontent.repositories.quiz.custom;

import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;

import java.util.List;

public interface QuizCustomRepository {

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов
	 */
	List<QuizWithTaskSizeEntity> getQuizListOrderedByShortName();

}
