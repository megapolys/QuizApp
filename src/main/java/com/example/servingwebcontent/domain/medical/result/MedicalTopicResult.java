package com.example.servingwebcontent.domain.medical.result;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.medical.MedicalTopic;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
public class MedicalTopicResult {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private MedicalTopic medicalTopic;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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

    public MedicalTopic getMedicalTopic() {
        return medicalTopic;
    }

    public void setMedicalTopic(MedicalTopic medicalTopic) {
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
