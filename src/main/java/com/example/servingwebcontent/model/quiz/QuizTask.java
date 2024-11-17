package com.example.servingwebcontent.model.quiz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizTask {

	/**
	 * Идентификатор вопроса
	 */
	private Long id;

	/**
	 * Порядковый номер отображения в интерфейсе
	 */
	private int position;

	/**
	 * Текст вопроса
	 */
	private String text;

	/**
	 * true - если нет решений, иначе false
	 */
	private boolean emptyDecisions;
}
