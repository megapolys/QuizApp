package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MedicalTaskWithDecisionsSize {

	/**
	 * Идентификатор анализа
	 */
	@NotNull
	Long id;

	/**
	 * Идентификатор топика анализов
	 */
	@NotNull
	Long topicId;

	/**
	 * Наименование
	 */
	@NotBlank
	String name;

	/**
	 * Единица измерения
	 */
	@NotBlank
	String unit;

	/**
	 * Количество левых решений
	 */
	@NotNull
	Long leftDecisionsSize;

	/**
	 * Количество правых решений
	 */
	@NotNull
	Long rightDecisionsSize;
}
