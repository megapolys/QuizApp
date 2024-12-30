package com.example.servingwebcontent.model.quiz.result;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizTaskCompleteCommand {

	@NotNull
	Long quizTaskResultId;

	@NotBlank
	String variant;

	String text;
}
