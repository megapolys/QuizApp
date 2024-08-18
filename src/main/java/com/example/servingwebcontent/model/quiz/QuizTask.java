package com.example.servingwebcontent.model.quiz;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import jakarta.persistence.*;

import java.util.Set;

public class QuizTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "quiz_id", referencedColumnName = "id", nullable = false)
	private QuizWithTaskSize quiz;

    private int position;

    @OneToOne(cascade = CascadeType.ALL)
    private FiveVariantTask fiveVariantTask;

    @OneToOne(cascade = CascadeType.ALL)
    private YesOrNoTask yesOrNoTask;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Decision> decisions;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

	public QuizWithTaskSize getQuiz() {
		return quiz;
	}

	public void setQuiz(QuizWithTaskSize quiz) {
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

	public Set<Decision> getDecisions() {
		return decisions;
	}

	public void setDecisions(Set<Decision> decisions) {
		this.decisions = decisions;
	}
}
