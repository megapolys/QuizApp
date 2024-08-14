package com.example.servingwebcontent.model.medical;

import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import jakarta.persistence.*;

import java.util.Set;

// При добавлении полей необходимо дорабатывать метод клонирование MedicalTopicService.clone()
public class MedicalTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MedicalTask> medicalTasks;

    @OneToMany(mappedBy = "medicalTopic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MedicalTopicResult> medicalTopicResults;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MedicalTask> getMedicalTasks() {
        return medicalTasks;
    }

    public void setMedicalTasks(Set<MedicalTask> medicalTasks) {
        this.medicalTasks = medicalTasks;
    }

    public Set<MedicalTopicResult> getMedicalTopicResults() {
        return medicalTopicResults;
    }

    public void setMedicalTopicResults(Set<MedicalTopicResult> medicalTopicResults) {
        this.medicalTopicResults = medicalTopicResults;
    }
}
