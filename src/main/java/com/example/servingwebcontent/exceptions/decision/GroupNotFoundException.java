package com.example.servingwebcontent.exceptions.decision;

public class GroupNotFoundException extends RuntimeException {
	public GroupNotFoundException(String message) {
		super(message);
	}

	public static GroupNotFoundException byId(Long id) {
		return new GroupNotFoundException("Group not found by id %d".formatted(id));
	}
}
