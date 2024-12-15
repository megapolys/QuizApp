package com.example.servingwebcontent.advice;

import com.example.servingwebcontent.exceptions.decision.DecisionAlreadyExistsException;
import com.example.servingwebcontent.exceptions.decision.GroupAlreadyExistsByNameException;
import com.example.servingwebcontent.exceptions.medical.MedicalTopicAlreadyExistsException;
import com.example.servingwebcontent.exceptions.quiz.QuizCreateException;
import com.example.servingwebcontent.model.dto.RestExceptionDto;
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

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected RestExceptionDto handleRuntimeException(Throwable t) {
		String localizedMessage = t.getLocalizedMessage();
		log.error("Exception: {}", localizedMessage, t);
		return new RestExceptionDto(t.getClass().getName(), localizedMessage);
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

	@ExceptionHandler(DecisionAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handle(DecisionAlreadyExistsException e) {
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

	@ExceptionHandler(QuizCreateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handleMethodArgumentNotValidException(QuizCreateException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return e.getPayload();
	}

	@ExceptionHandler(MedicalTopicAlreadyExistsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ValidationErrorDto handle(MedicalTopicAlreadyExistsException e) {
		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
		return e.getPayload();
	}
}
