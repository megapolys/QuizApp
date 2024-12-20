package com.example.servingwebcontent.model.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
	@Size(max = 5)
	String shortName;
}
