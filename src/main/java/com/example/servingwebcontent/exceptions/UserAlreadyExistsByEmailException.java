package com.example.servingwebcontent.exceptions;

public class UserAlreadyExistsByEmailException extends RuntimeException {
	public UserAlreadyExistsByEmailException(String message) {
		super(message);
	}

	public static UserAlreadyExistsByEmailException byEmail(String email) {
		return new UserAlreadyExistsByEmailException("User already exists by email %s".formatted(email));
	}
}
