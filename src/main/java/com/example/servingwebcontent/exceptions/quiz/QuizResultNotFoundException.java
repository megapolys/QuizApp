package com.example.servingwebcontent.exceptions.quiz;

public class QuizResultNotFoundException extends RuntimeException {
	public QuizResultNotFoundException(String message) {
		super(message);
	}

	public static QuizResultNotFoundException byId(Long id) {
		return new QuizResultNotFoundException("Quiz result not found by id: %d".formatted(id));
	}
}
