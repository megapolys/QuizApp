package com.example.servingwebcontent.exceptions.medical;

public class MedicalTopicNotFoundException extends RuntimeException {

	public MedicalTopicNotFoundException(String message) {
		super(message);
	}

	public static MedicalTopicNotFoundException byId(Long id) {
		return new MedicalTopicNotFoundException("Medical topic not found by id %d".formatted(id));
	}
}
