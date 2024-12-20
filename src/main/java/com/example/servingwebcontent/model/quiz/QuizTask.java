package com.example.servingwebcontent.model.quiz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizTask {

	/**
	 * Идентификатор вопроса
	 */
	Long id;

	/**
	 * Порядковый номер отображения в интерфейсе
	 */
	int position;

	/**
	 * Текст вопроса
	 */
	String text;

	/**
	 * Количество решений
	 */
	Long decisionsCount;
}
