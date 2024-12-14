package com.example.servingwebcontent.exceptions.medical;

public class MedicalTopicAlreadyExistsException extends RuntimeException {

	public MedicalTopicAlreadyExistsException(String message) {
		super(message);
	}

	public static MedicalTopicAlreadyExistsException byName(String name) {
		return new MedicalTopicAlreadyExistsException("Medical topic already exists by name %s".formatted(name));
	}
}
