package com.example.servingwebcontent.exceptions.user;

public class RoleNotFoundException extends RuntimeException {
	public RoleNotFoundException(String message) {
		super(message);
	}

	public static RoleNotFoundException byName(String name) {
		return new RoleNotFoundException("Role not found by name %s".formatted(name));
	}
}
