package com.example.servingwebcontent.model.quiz.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

import static com.example.servingwebcontent.consts.Consts.DATE_TIME_FORMAT;
import static com.example.servingwebcontent.consts.Consts.ZONE;

@Value
@Builder
public class QuizResultCalculated {

    Long id;

    boolean complete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT, timezone = ZONE)
    Instant completeDate;

    float score;

    Long countCompleted;

    Integer taskCount;
}
