package com.example.servingwebcontent.model.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ValidationErrorDto {

	List<FieldDto> fields;

	@Value
	@Builder
	public static class FieldDto {
		String fieldName;
		String error;
	}
}
