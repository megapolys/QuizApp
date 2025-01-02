package com.example.servingwebcontent.model.medical.result;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class MedicalTopicResult {

	Long id;

	Long topicId;

	Long userId;

	Instant completeDate;

	Instant lastUpdateDate;
}
