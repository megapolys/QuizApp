package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizWithResults;

import java.util.List;

public interface QuizResultService {

	/**
	 * Получить список вычисленных результатов тестов
	 *
	 * @param userId идентификатор профиля
	 *
	 * @return список результатов тестов
	 */
	List<QuizWithResults> getQuizResultListByUserId(Long userId);
}
