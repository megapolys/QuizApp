package com.example.servingwebcontent.model.quiz.result;

import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.user.User;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean complete;
    @Column(name = "complete_date")
    private Date completeDate;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<QuizTaskResult> taskList;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Set<QuizTaskResult> getTaskList() {
        return taskList;
    }

    public void setTaskList(Set<QuizTaskResult> taskList) {
        this.taskList = taskList;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
}
