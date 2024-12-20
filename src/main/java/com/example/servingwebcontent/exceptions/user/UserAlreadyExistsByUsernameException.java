package com.example.servingwebcontent.exceptions.user;

public class UserAlreadyExistsByUsernameException extends RuntimeException {
	public UserAlreadyExistsByUsernameException(String message) {
		super(message);
	}

	public static UserAlreadyExistsByUsernameException byUsername(String username) {
		return new UserAlreadyExistsByUsernameException("User already exists by username %s".formatted(username));
	}
}
