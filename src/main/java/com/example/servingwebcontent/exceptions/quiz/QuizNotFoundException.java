package com.example.servingwebcontent.exceptions.quiz;

public class QuizNotFoundException extends RuntimeException {
	public QuizNotFoundException(String message) {
		super(message);
	}

	public static QuizNotFoundException byId(Long id) {
		return new QuizNotFoundException("Quiz not found by id: %d".formatted(id));
	}
}
