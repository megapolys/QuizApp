package com.example.servingwebcontent.model.quiz.result;

import com.example.servingwebcontent.model.decision.Decision;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DecisionCalculated {

	Decision decision;

	float score;

	float altScore;

	int count;

}
