package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MedicalTaskUpdateCommandDto {

	/**
	 * Идентификатор топика анализов
	 */
	@NotNull
	Long topicId;

	/**
	 * Идентификатор анализа
	 */
	@NotNull
	Long taskId;

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
	 * Список идентификаторов левых решений
	 */
	List<Long> leftDecisionIds;

	/**
	 * Список идентификаторов правых решений
	 */
	List<Long> rightDecisionIds;

}
