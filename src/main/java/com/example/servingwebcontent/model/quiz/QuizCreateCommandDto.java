package com.example.servingwebcontent.model.quiz;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizCreateCommandDto {

	/**
	 * Полное наименование теста
	 */
	@NotBlank
	String name;

	/**
	 * Кратное наименование теста
	 */
	@NotBlank
	@Max(5)
	String shortName;
}
