package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.repositories.QuizDecisionRepository;
import com.example.servingwebcontent.repositories.QuizTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class QuizDecisionService {

    private final QuizDecisionRepository quizDecisionRepository;
    private final QuizTaskRepository quizTaskRepository;

    public QuizDecisionService(QuizDecisionRepository quizDecisionRepository, QuizTaskRepository quizTaskRepository) {
        this.quizDecisionRepository = quizDecisionRepository;
        this.quizTaskRepository = quizTaskRepository;
    }

    public DecisionResult add(QuizDecision decision) {
        final QuizDecision byName = quizDecisionRepository.findByName(decision.getName());
        if (byName != null) {
            return new DecisionResult(ResultType.NAME_FOUND, byName);
        }
        return new DecisionResult(ResultType.SUCCESS, quizDecisionRepository.save(decision));
    }

    public void delete(QuizDecision decision) {
        for (QuizTask task : quizTaskRepository.findAll()) {
            if (task.getDecisions().remove(decision)) {
                quizTaskRepository.save(task);
            }
        }
        quizDecisionRepository.delete(decision);
    }

    public enum ResultType {
        NAME_FOUND, SUCCESS
    }

    public record DecisionResult(ResultType result, QuizDecision decision) {
    }

}
