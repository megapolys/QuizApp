package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizDecisionRepository extends CrudRepository<QuizDecision, Long> {
    QuizDecision findByName(String name);

    List<QuizDecision> findAllByOrderByName();
}
