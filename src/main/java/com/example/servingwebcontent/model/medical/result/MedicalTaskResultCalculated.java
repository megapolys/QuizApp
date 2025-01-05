package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.medical.MedicalTask;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MedicalTaskResultCalculated {

    Long id;

    MedicalTask task;

    Long topicResultId;

    Float value;

    Float score;

    List<DecisionWithGroup> decisions;
}
