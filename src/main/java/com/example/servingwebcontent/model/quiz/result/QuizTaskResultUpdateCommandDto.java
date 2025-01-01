package com.example.servingwebcontent.model.quiz.result;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuizTaskResultUpdateCommandDto {

	@NotNull
	Long quizTaskResultId;

	Float altScore;
}
