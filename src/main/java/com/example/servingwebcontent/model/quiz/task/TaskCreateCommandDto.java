package com.example.servingwebcontent.model.quiz.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TaskCreateCommandDto {

	/**
	 * Идентификатор теста
	 */
	@NotNull
	Long quizId;

	/**
	 * Порядковый номер
	 */
	@NotNull
	@Min(1)
	Integer position;

	/**
	 * Текст задания
	 */
	@NotBlank
	String preQuestionText;

	/**
	 * Текст вопроса
	 */
	@NotBlank
	String questionText;

	/**
	 * Список идентификаторов решений
	 */
	List<Long> decisionIds;

	/**
	 * Веса для 5 вариантов
	 */
	FiveVariantCreateCommandDto fiveVariant;

	/**
	 * Веса для 2 вариантов
	 */
	YesOrNoCreateCommandDto yesOrNo;
}
