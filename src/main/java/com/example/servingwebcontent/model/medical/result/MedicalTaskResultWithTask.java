package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.medical.MedicalTask;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MedicalTaskResultWithTask {

    Long id;

    MedicalTask task;

    Long topicResultId;

    Float value;

    Float altScore;
}
