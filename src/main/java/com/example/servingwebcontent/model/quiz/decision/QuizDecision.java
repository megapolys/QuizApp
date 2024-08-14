package com.example.servingwebcontent.model.quiz.decision;

import jakarta.persistence.*;

@Entity(name = "decision")
public class QuizDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @Column(length = 2000)
    private String description;
    @ManyToOne
    private DecisionGroup group;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DecisionGroup getGroup() {
        return group;
    }

    public void setGroup(DecisionGroup group) {
        this.group = group;
    }
}
