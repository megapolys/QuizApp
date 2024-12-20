package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicalTaskFull {

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
	 * Левый референс
	 */
	@NotNull
	Float leftLeft;

	/**
	 * Левый оптимум
	 */
	@NotNull
	Float leftMid;

	/**
	 * Правый оптимум
	 */
	@NotNull
	Float rightMid;

	/**
	 * Правый референс
	 */
	@NotNull
	Float rightRight;

	/**
	 * Список левых решений
	 */
	@NotNull
	List<Long> leftDecisions;

	/**
	 * Список правых решений
	 */
	@NotNull
	List<Long> rightDecisions;
}
