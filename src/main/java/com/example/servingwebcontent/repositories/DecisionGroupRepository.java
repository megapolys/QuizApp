package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DecisionGroupRepository extends CrudRepository<DecisionGroup, Long> {
    List<DecisionGroup> findAllByOrderByName();

    DecisionGroup findByName(String name);
}
