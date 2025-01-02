package com.example.servingwebcontent.model.medical.result;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicalTaskResult {

	Long id;

	Long taskId;

	Long topicResultId;

	Float value;

	Float altScore;
}
