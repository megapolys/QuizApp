package com.example.servingwebcontent.domain.medical.result;

import com.example.servingwebcontent.domain.medical.MedicalTask;
import jakarta.persistence.*;

@Entity
public class MedicalTaskResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MedicalTask medicalTask;

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
}
