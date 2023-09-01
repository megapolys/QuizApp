package com.example.servingwebcontent.domain.medical;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class MedicalTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<MedicalTask> medicalTasks;

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
}
