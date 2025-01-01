package com.example.servingwebcontent.model.quiz.result;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuizTaskResultCalculated {

    Long id;

    Integer position;

    String questionText;

    Float altScore;

    Float resultScore;

    String text;
}
