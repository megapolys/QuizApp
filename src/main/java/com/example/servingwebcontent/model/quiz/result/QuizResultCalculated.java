package com.example.servingwebcontent.model.quiz.result;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class QuizResultCalculated {

    Long id;

    boolean complete;

    Instant completeDate;

    float score;

    Long countCompleted;

    Integer taskCount;
}
