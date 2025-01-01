package com.example.servingwebcontent.model.quiz.result;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class QuizFullResultDto {

	String quizName;

	List<DecisionCalculated> decisions;

	List<QuizTaskResultCalculated> results;

}
