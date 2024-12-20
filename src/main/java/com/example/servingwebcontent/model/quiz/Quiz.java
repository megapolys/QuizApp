package com.example.servingwebcontent.model.quiz;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Quiz {

	/**
	 * Идентификатор теста
	 */
	Long id;

	/**
	 * Полное наименование теста
	 */
	String name;

	/**
	 * Кратное наименование теста
	 */
	String shortName;
}
