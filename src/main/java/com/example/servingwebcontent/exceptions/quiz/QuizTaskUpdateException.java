package com.example.servingwebcontent.exceptions.quiz;

public class QuizTaskUpdateException extends RuntimeException {
	public QuizTaskUpdateException(String message) {
		super(message);
	}

	public static QuizTaskUpdateException byVariants() {
		return new QuizTaskUpdateException("Quiz task cant update with fiveVariant and yesOrNo both");
	}
}
