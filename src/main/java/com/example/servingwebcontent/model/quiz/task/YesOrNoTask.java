package com.example.servingwebcontent.model.quiz.task;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class YesOrNoTask {

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
     * Вес для положительного ответа
     */
    private Float yesWeight;

    /**
     * Вес для отрицательного ответа
     */
    private Float noWeight;
}
