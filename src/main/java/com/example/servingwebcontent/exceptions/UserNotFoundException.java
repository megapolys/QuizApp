package com.example.servingwebcontent.exceptions;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(String message) {
		super(message);
	}

	public static UserNotFoundException byId(Long id) {
		return new UserNotFoundException("User not found by id %d".formatted(id));
	}

	public static UserNotFoundException byEmail(String email) {
		return new UserNotFoundException("User not found by email %s".formatted(email));
	}

	public static UserNotFoundException byRepairPasswordCode(String repairPasswordCode) {
		return new UserNotFoundException("User not found by repair password code %s".formatted(repairPasswordCode));
	}

	public static UserNotFoundException byLogin(String login) {
		return new UserNotFoundException("User not found by login %s".formatted(login));
	}
}
