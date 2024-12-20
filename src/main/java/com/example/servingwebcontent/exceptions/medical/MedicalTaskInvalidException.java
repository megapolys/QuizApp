package com.example.servingwebcontent.exceptions.medical;

import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import lombok.Getter;

import java.util.stream.Stream;

import static com.example.servingwebcontent.consts.MessageConsts.MEDICAL_TASK_INVALID_REFERENCE;

@Getter
public class MedicalTaskInvalidException extends RuntimeException {

	private final ValidationErrorDto payload;

	public MedicalTaskInvalidException(String message, ValidationErrorDto payload) {
		super(message);
		this.payload = payload;
	}

	public static MedicalTaskInvalidException byReference() {
		return new MedicalTaskInvalidException(
			"Medical task invalid reference",
			ValidationErrorDto.builder()
				.fields(Stream.of("leftLeft", "leftMid", "rightMid", "rightRight")
					.map(fieldName -> ValidationErrorDto.FieldDto.builder()
						.fieldName(fieldName)
						.error(MEDICAL_TASK_INVALID_REFERENCE)
						.build())
					.toList())
				.build()
		);
	}
}
