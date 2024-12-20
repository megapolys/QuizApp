package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
public class MethodArgumentNotValidExceptionToValidationErrorDtoConverter
	implements Converter<MethodArgumentNotValidException, ValidationErrorDto> {
	@Override
	public ValidationErrorDto convert(MethodArgumentNotValidException exception) {
		return ValidationErrorDto.builder()
			.fields(exception.getBindingResult().getAllErrors().stream()
				.map(error -> ValidationErrorDto.FieldDto.builder()
					.fieldName(((FieldError) error).getField())
					.error(error.getDefaultMessage())
					.build()
				).toList())
			.build();
	}
}
