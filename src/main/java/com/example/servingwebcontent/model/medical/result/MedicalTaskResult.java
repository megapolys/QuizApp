package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.medical.MedicalTask;
import jakarta.persistence.*;

@Entity
public class MedicalTaskResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_task_id")
    private MedicalTask medicalTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_result_id")
    private MedicalTopicResult topicResult;

    private Float value;

    private Float altScore;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public MedicalTask getMedicalTask() {
        return medicalTask;
    }

    public void setMedicalTask(MedicalTask medicalTask) {
        this.medicalTask = medicalTask;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getAltScore() {
        return altScore;
    }

    public void setAltScore(Float altScore) {
        this.altScore = altScore;
    }

    public MedicalTopicResult getTopicResult() {
        return topicResult;
    }

    public void setTopicResult(MedicalTopicResult topicResult) {
        this.topicResult = topicResult;
    }
}
