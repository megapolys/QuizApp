package com.example.servingwebcontent.model.quiz;

import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuizTaskFull {

	/**
	 * Идентификатор вопроса
	 */
	Long id;

	/**
	 * Идентификатор теста
	 */
	Long quizId;

	/**
	 * Порядковый номер отображения в интерфейсе
	 */
	int position;

	/**
	 * Список идентификаторов решений
	 */
	List<Long> decisionIds;

	/**
	 * Вопрос в пять вариантов
	 */
	FiveVariantTask fiveVariantTask;

	/**
	 * Да/нет вопрос
	 */
	YesOrNoTask yesOrNoTask;
}
