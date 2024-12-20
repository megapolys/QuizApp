package com.example.servingwebcontent.exceptions;

public class FileUploadException extends RuntimeException {

	public FileUploadException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public static FileUploadException quizTaskFile(Throwable throwable) {
		return new FileUploadException("Task upload exceptions", throwable);
	}
}
