package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class MedicalTopicCreateCommandDto {

	/**
	 * Наименование
	 */
	@NotBlank
	String name;
}
