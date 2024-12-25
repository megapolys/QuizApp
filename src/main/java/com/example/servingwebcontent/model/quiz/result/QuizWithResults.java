package com.example.servingwebcontent.model.quiz.result;

import com.example.servingwebcontent.model.quiz.Quiz;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class QuizWithResults {

	Quiz quiz;

	List<QuizResultCalculated> results;

}
