package com.example.servingwebcontent.model.entities.medical.result;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicalTaskResultWithTaskEntity {

	Long id;

	MedicalTaskEntity task;

	Long topicResultId;

	Float value;
}
