package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalTopicUpdateCommandDto {

	/**
	 * Идентификатор
	 */
	@NotNull
	Long id;

	/**
	 * Наименование
	 */
	@NotBlank
	String name;
}
