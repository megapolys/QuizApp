package com.example.servingwebcontent.domain.quiz.decision;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class DecisionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    @OneToMany
    private Set<QuizDecision> decisions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<QuizDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<QuizDecision> decisions) {
        this.decisions = decisions;
    }
}
