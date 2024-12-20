package com.example.servingwebcontent.model.quiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizUpdateCommandDto {

	/**
	 * Идентификатор теста
	 */
	@NotNull
	Long id;

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
