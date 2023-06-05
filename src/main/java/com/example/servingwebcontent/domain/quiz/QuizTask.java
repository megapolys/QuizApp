package com.example.servingwebcontent.domain.quiz;

import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class QuizTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "quiz_id", referencedColumnName = "id", nullable = false)
    private Quiz quiz;

    private int position;

    @OneToOne(cascade = CascadeType.ALL)
    private FiveVariantTask fiveVariantTask;

    @OneToOne(cascade = CascadeType.ALL)
    private YesOrNoTask yesOrNoTask;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<QuizDecision> decisions;

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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FiveVariantTask getFiveVariantTask() {
        return fiveVariantTask;
    }

    public void setFiveVariantTask(FiveVariantTask fiveVariantTask) {
        this.fiveVariantTask = fiveVariantTask;
    }

    public YesOrNoTask getYesOrNoTask() {
        return yesOrNoTask;
    }

    public void setYesOrNoTask(YesOrNoTask yesOrNoTask) {
        this.yesOrNoTask = yesOrNoTask;
    }

    public Set<QuizDecision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<QuizDecision> decisions) {
        this.decisions = decisions;
    }
}
