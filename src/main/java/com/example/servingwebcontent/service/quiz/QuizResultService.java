package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizFullResultDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
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

	/**
	 * Получить полный резлуьтат теста по идентификатору результата теста
	 *
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return полный результат теста
	 */
	QuizFullResultDto getQuizResultById(Long quizResultId);

	/**
	 * Удаление результата теста по идентификатору
	 *
	 * @param quizResultId идентификатор результата теста
	 */
	void deleteQuizResultById(Long quizResultId);

	/**
	 * Получение результата теста
	 *
	 * @param quizTaskResultId идентификатор результата теста
	 *
	 * @return результат теста
	 */
	QuizTaskResultWithTaskType getQuizTaskResultById(Long quizTaskResultId);

	/**
	 * Изменить значение для результата вопроса
	 *
	 * @param command команда для обновления
	 */
	void updateQuizTaskResult(QuizTaskResultUpdateCommandDto command);
}
