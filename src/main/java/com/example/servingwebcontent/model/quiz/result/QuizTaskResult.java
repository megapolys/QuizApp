package com.example.servingwebcontent.model.quiz.result;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QuizTaskResult {

    Long id;

    boolean complete;

    String variant;

    Float altScore;

    String text;
}
