package com.example.servingwebcontent.exceptions.medical;

import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import lombok.Getter;

import java.util.List;

import static com.example.servingwebcontent.consts.MessageConsts.MEDICAL_TOPIC_ALREADY_EXISTS_BY_NAME;

@Getter
public class MedicalTopicAlreadyExistsException extends RuntimeException {

	private final ValidationErrorDto payload;

	public MedicalTopicAlreadyExistsException(String message, ValidationErrorDto payload) {
		super(message);
		this.payload = payload;
	}

	public static MedicalTopicAlreadyExistsException byName(String name) {
		return new MedicalTopicAlreadyExistsException(
			"Medical topic already exists by name %s".formatted(name),
			ValidationErrorDto.builder()
				.fields(List.of(ValidationErrorDto.FieldDto.builder()
					.fieldName("name")
					.error(MEDICAL_TOPIC_ALREADY_EXISTS_BY_NAME)
					.build()))
				.build()
		);
	}
}
