package com.example.servingwebcontent.exceptions.quiz;

public class QuizTaskNotFoundException extends RuntimeException {
	public QuizTaskNotFoundException(String message) {
		super(message);
	}

	public static QuizTaskNotFoundException byId(Long id) {
		return new QuizTaskNotFoundException("Quiz task not found by id: %d".formatted(id));
	}
}
