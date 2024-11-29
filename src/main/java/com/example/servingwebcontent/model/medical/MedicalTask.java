package com.example.servingwebcontent.model.medical;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.medical.result.MedicalTaskResult;
import jakarta.persistence.*;

import java.util.Set;

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
	private Set<Decision> leftDecisions;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Decision> rightDecisions;

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

	public Set<Decision> getLeftDecisions() {
		return leftDecisions;
	}

	public void setLeftDecisions(Set<Decision> leftDecisions) {
		this.leftDecisions = leftDecisions;
	}

	public Set<Decision> getRightDecisions() {
		return rightDecisions;
	}

	public void setRightDecisions(Set<Decision> rightDecisions) {
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