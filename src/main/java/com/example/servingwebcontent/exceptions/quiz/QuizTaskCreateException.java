package com.example.servingwebcontent.exceptions.quiz;

public class QuizTaskCreateException extends RuntimeException {
	public QuizTaskCreateException(String message) {
		super(message);
	}

	public static QuizTaskCreateException byVariants() {
		return new QuizTaskCreateException("Quiz task cant create with fiveVariant and yesOrNo both");
	}
}
