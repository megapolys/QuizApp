package com.example.servingwebcontent.domain.medical;

import com.example.servingwebcontent.domain.medical.result.MedicalTaskResult;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import jakarta.persistence.*;

import java.util.Set;

@Entity
// При добавлении полей необходимо дорабатывать метод клонирование MedicalTopicService.clone()
public class MedicalTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private MedicalTopic topic;

    @OneToMany(mappedBy = "medicalTask", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MedicalTaskResult> medicalTaskResults;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<QuizDecision> leftDecisions;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<QuizDecision> rightDecisions;

    private Float leftLeft;
    private Float leftMid;
    private Float rightMid;
    private Float rightRight;

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

    public Float getLeftLeft() {
        return leftLeft;
    }

    public void setLeftLeft(Float leftLeft) {
        this.leftLeft = leftLeft;
    }

    public Float getLeftMid() {
        return leftMid;
    }

    public void setLeftMid(Float leftMid) {
        this.leftMid = leftMid;
    }

    public Float getRightMid() {
        return rightMid;
    }

    public void setRightMid(Float rightMid) {
        this.rightMid = rightMid;
    }

    public Float getRightRight() {
        return rightRight;
    }

    public void setRightRight(Float rightRight) {
        this.rightRight = rightRight;
    }

    public Set<QuizDecision> getLeftDecisions() {
        return leftDecisions;
    }

    public void setLeftDecisions(Set<QuizDecision> leftDecisions) {
        this.leftDecisions = leftDecisions;
    }

    public Set<QuizDecision> getRightDecisions() {
        return rightDecisions;
    }

    public void setRightDecisions(Set<QuizDecision> rightDecisions) {
        this.rightDecisions = rightDecisions;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public MedicalTopic getTopic() {
        return topic;
    }

    public void setTopic(MedicalTopic topic) {
        this.topic = topic;
    }

    public Set<MedicalTaskResult> getMedicalTaskResults() {
        return medicalTaskResults;
    }

    public void setMedicalTaskResults(Set<MedicalTaskResult> medicalTaskResults) {
        this.medicalTaskResults = medicalTaskResults;
    }
}
