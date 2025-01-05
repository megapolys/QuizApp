package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DecisionCalculated {

	DecisionWithGroup decision;

	float score;

	int count;

}
