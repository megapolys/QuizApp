package com.example.servingwebcontent.model.medical;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MedicalTopicCreateCommandDto {

	/**
	 * Наименование
	 */
	@NotBlank
	String name;
}
