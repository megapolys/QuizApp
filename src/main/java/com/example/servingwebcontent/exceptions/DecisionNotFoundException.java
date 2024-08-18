package com.example.servingwebcontent.exceptions;

public class DecisionNotFoundException extends RuntimeException {
	public DecisionNotFoundException(String message) {
		super(message);
	}

	public static DecisionNotFoundException byId(Long id) {
		return new DecisionNotFoundException("Decision not found by id %d".formatted(id));
	}
}
