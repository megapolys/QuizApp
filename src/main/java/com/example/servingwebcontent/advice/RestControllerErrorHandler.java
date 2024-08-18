package com.example.servingwebcontent.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	protected ValidationErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//		log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage(), e);
//		return mvcConversionService.convert(e, ValidationErrorDto.class);
//	}

}
