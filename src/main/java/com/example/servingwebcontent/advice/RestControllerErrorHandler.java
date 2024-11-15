package com.example.servingwebcontent.advice;

import com.example.servingwebcontent.exceptions.DecisionAlreadyExistsByNameException;
import com.example.servingwebcontent.exceptions.GroupAlreadyExistsByNameException;
import com.example.servingwebcontent.model.dto.ValidationErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static com.example.servingwebcontent.consts.MessageConsts.DECISION_WITH_SAME_NAME_ALREADY_EXISTS;
import static com.example.servingwebcontent.consts.MessageConsts.GROUP_WITH_SAME_NAME_ALREADY_EXISTS;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestControllerErrorHandler {

	private final ConversionService mvcConversionService;

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected String handleRuntimeException(RuntimeException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return "error";
	}

	@ExceptionHandler(GroupAlreadyExistsByNameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handle(GroupAlreadyExistsByNameException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return ValidationErrorDto.builder()
			.fields(List.of(ValidationErrorDto.FieldDto.builder()
				.fieldName("name")
				.error(GROUP_WITH_SAME_NAME_ALREADY_EXISTS)
				.build()))
			.build();
	}

	@ExceptionHandler(DecisionAlreadyExistsByNameException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handle(DecisionAlreadyExistsByNameException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return ValidationErrorDto.builder()
			.fields(List.of(ValidationErrorDto.FieldDto.builder()
				.fieldName("name")
				.error(DECISION_WITH_SAME_NAME_ALREADY_EXISTS)
				.build()))
			.build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return mvcConversionService.convert(e, ValidationErrorDto.class);
	}

}
