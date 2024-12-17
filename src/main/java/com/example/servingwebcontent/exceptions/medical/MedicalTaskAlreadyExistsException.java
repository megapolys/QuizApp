package com.example.servingwebcontent.exceptions.medical;

import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import lombok.Getter;

import java.util.List;

import static com.example.servingwebcontent.consts.MessageConsts.MEDICAL_TASK_ALREADY_EXISTS_BY_NAME;

@Getter
public class MedicalTaskAlreadyExistsException extends RuntimeException {

	private final ValidationErrorDto payload;

	public MedicalTaskAlreadyExistsException(String message, ValidationErrorDto payload) {
		super(message);
		this.payload = payload;
	}

	public static MedicalTaskAlreadyExistsException byName(String name) {
		return new MedicalTaskAlreadyExistsException(
			"Medical task already exists by name %s".formatted(name),
			ValidationErrorDto.builder()
				.fields(List.of(ValidationErrorDto.FieldDto.builder()
					.fieldName("name")
					.error(MEDICAL_TASK_ALREADY_EXISTS_BY_NAME)
					.build()))
				.build()
		);
	}
}
