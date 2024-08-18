package com.example.servingwebcontent.exceptions;

public class DecisionAlreadyExistsByNameException extends RuntimeException {
	public DecisionAlreadyExistsByNameException(String message) {
		super(message);
	}

	public static DecisionAlreadyExistsByNameException byName(String name) {
		return new DecisionAlreadyExistsByNameException("Group already exists by name %s".formatted(name));
	}
}
