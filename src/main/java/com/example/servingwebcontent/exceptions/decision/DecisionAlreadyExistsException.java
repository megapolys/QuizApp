package com.example.servingwebcontent.exceptions.decision;

public class DecisionAlreadyExistsException extends RuntimeException {

	public DecisionAlreadyExistsException(String message) {
		super(message);
	}

	public static DecisionAlreadyExistsException byName(String name) {
		return new DecisionAlreadyExistsException("Group already exists by name %s".formatted(name));
	}
}
