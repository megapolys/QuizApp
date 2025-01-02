package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.medical.MedicalTopic;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MedicalWithResults {

	MedicalTopic topic;

	List<MedicalResultCalculated> results;
}
