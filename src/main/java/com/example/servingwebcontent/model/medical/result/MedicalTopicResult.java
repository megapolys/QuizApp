package com.example.servingwebcontent.model.medical.result;

import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.model.user.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class MedicalTopicResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne
	@JoinColumn(name = "medical_topic_id")
	private MedicalTopicWithTaskSize medicalTopic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "topicResult", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<MedicalTaskResult> results;

    @Column(name = "complete_date")
    private Date completeDate;
    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

	public MedicalTopicWithTaskSize getMedicalTopic() {
		return medicalTopic;
	}

	public void setMedicalTopic(MedicalTopicWithTaskSize medicalTopic) {
		this.medicalTopic = medicalTopic;
	}

    public Set<MedicalTaskResult> getResults() {
        return results;
    }

    public void setResults(Set<MedicalTaskResult> results) {
        this.results = results;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
