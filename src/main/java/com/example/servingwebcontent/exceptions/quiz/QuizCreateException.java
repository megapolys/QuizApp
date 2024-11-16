package com.example.servingwebcontent.exceptions.quiz;

import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import lombok.Getter;

import java.util.List;

import static com.example.servingwebcontent.consts.MessageConsts.QUIZ_WITH_SHORT_NAME_ALREADY_EXISTS;

@Getter
public class QuizCreateException extends RuntimeException {

	private final ValidationErrorDto payload;

	public QuizCreateException(ValidationErrorDto payload) {
		this.payload = payload;
	}

	public static QuizCreateException alreadyExistsByShortName(String shortName) {
		ValidationErrorDto payload = ValidationErrorDto.builder()
				.fields(List.of(ValidationErrorDto.FieldDto.builder()
										.fieldName("shortName")
										.error(QUIZ_WITH_SHORT_NAME_ALREADY_EXISTS)
										.build()))
				.build();
		return new QuizCreateException(payload);
	}
}
