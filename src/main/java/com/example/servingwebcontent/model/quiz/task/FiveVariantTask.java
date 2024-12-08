package com.example.servingwebcontent.model.quiz.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiveVariantTask {

	/**
	 * Идентификатор
	 */
	private Long id;

	/**
	 * Первичный вопрос
	 */
	private String preQuestionText;

	/**
	 * Текст вопроса
	 */
	private String questionText;

	/**
	 * Наименование файла
	 */
	private String fileName;

	/**
	 * Первый вес
	 */
	private Float firstWeight;

	/**
	 * Второй вес
	 */
	private Float secondWeight;

	/**
	 * Третий вес
	 */
	private Float thirdWeight;

	/**
	 * Четвертый вес
	 */
	private Float fourthWeight;

	/**
	 * Пятый вес
	 */
	private Float fifthWeight;

}
