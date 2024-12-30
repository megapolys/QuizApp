package com.example.servingwebcontent.exceptions;

public class AccessDeniedException extends RuntimeException {

	public AccessDeniedException(String message) {
		super(message);
	}

	public static AccessDeniedException quizByUserId(Long userId, Long quizResultId) {
		return new AccessDeniedException("User with id %d try to access quizResultId %d".formatted(userId, quizResultId));
	}

	public static AccessDeniedException quizTaskByUserId(Long userId, Long quizTaskResultId) {
		return new AccessDeniedException("User with id %d try to access quizTaskResultId %d".formatted(userId, quizTaskResultId));
	}
}
