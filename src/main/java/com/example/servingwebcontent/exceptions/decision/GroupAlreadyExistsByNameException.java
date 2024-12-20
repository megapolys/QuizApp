package com.example.servingwebcontent.exceptions.decision;

public class GroupAlreadyExistsByNameException extends RuntimeException {
	public GroupAlreadyExistsByNameException(String message) {
		super(message);
	}

	public static GroupAlreadyExistsByNameException byName(String name) {
		return new GroupAlreadyExistsByNameException("Group already exists by name %s".formatted(name));
	}
}
