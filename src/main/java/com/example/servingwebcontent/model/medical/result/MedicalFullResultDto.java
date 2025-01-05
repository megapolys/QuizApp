package com.example.servingwebcontent.model.medical.result;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MedicalFullResultDto {

	String topicName;

	List<DecisionCalculated> decisions;

	List<MedicalTaskResultCalculated> results;

}
