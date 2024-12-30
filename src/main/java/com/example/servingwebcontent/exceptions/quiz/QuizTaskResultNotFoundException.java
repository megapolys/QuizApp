package com.example.servingwebcontent.exceptions.quiz;

public class QuizTaskResultNotFoundException extends RuntimeException {
	public QuizTaskResultNotFoundException(String message) {
		super(message);
	}

	public static QuizTaskResultNotFoundException byId(Long id) {
		return new QuizTaskResultNotFoundException("Quiz task result not found by id: %d".formatted(id));
	}
}
