package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.repositories.DecisionGroupRepository;
import com.example.servingwebcontent.repositories.QuizDecisionRepository;
import com.example.servingwebcontent.repositories.QuizTaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class QuizDecisionService {

    private final QuizDecisionRepository quizDecisionRepository;
    private final DecisionGroupRepository decisionGroupRepository;
    private final QuizTaskRepository quizTaskRepository;

    public QuizDecisionService(QuizDecisionRepository quizDecisionRepository, DecisionGroupRepository decisionGroupRepository, QuizTaskRepository quizTaskRepository) {
        this.quizDecisionRepository = quizDecisionRepository;
        this.decisionGroupRepository = decisionGroupRepository;
        this.quizTaskRepository = quizTaskRepository;
    }

    public List<QuizDecision> decisionsWithoutGroups() {
        return decisions().stream().filter((d) -> d.getGroup() == null).toList();
    }

    private List<QuizDecision> decisions() {
        return quizDecisionRepository.findAllByOrderByName();
    }

    public List<DecisionGroup> groups() {
        final List<DecisionGroup> groups = decisionGroupRepository.findAllByOrderByName();
        for (DecisionGroup group : groups) {
            group.setDecisions(group.getDecisions().stream().sorted(Comparator.comparing(QuizDecision::getName)).collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return groups;
    }

    public ResultType add(QuizDecision decision) {
        final QuizDecision byName = quizDecisionRepository.findByName(decision.getName());
        if (byName != null) {
            return ResultType.NAME_FOUND;
        }
        if (decision.getGroup() != null) {
            final DecisionGroup group = decisionGroupRepository.findById(decision.getGroup().getId()).orElseThrow();
            group.getDecisions().add(decision);
        }
        quizDecisionRepository.save(decision);
        return ResultType.SUCCESS;
    }

    public ResultType add(DecisionGroup group) {
        final DecisionGroup byName = decisionGroupRepository.findByName(group.getName());
        if (byName != null) {
            return ResultType.NAME_FOUND;
        }
        decisionGroupRepository.save(group);
        return ResultType.SUCCESS;
    }

    public void delete(QuizDecision decision) {
        for (QuizTask task : quizTaskRepository.findAll()) {
            if (task.getDecisions().remove(decision)) {
                quizTaskRepository.save(task);
            }
        }
        if (decision.getGroup() != null) {
            decision.getGroup().getDecisions().remove(decision);
            decisionGroupRepository.save(decision.getGroup());
        }
        quizDecisionRepository.delete(decision);
    }

    public void delete(DecisionGroup group) {
        for (QuizDecision decision : quizDecisionRepository.findAll()) {
            if (decision.getGroup() != null && Objects.equals(decision.getGroup().getId(), group.getId())) {
                decision.setGroup(null);
                quizDecisionRepository.save(decision);
            }
        }
        decisionGroupRepository.delete(group);
    }

    public ResultType updateDecision(QuizDecision decision, DecisionGroup oldGroup) {
        final QuizDecision byName = quizDecisionRepository.findByName(decision.getName());
        if (byName != null && byName != decision) {
            return ResultType.NAME_FOUND;
        }
        if (oldGroup == null) {
            updateGroup(decision);
        }
        if (oldGroup != null && !Objects.equals(decision.getGroup().getId(), oldGroup.getId())) {
            updateGroup(decision);
            oldGroup.getDecisions().removeIf(d -> Objects.equals(d.getId(), decision.getId()));
            decisionGroupRepository.save(oldGroup);
        }
        quizDecisionRepository.save(decision);
        return ResultType.SUCCESS;
    }

    private void updateGroup(QuizDecision decision) {
        if (decision.getGroup() != null) {
            final DecisionGroup group = decisionGroupRepository.findById(decision.getGroup().getId()).orElseThrow();
            group.getDecisions().add(decision);
            decisionGroupRepository.save(group);
        }
    }

    public ResultType updateGroup(DecisionGroup group) {
        final DecisionGroup byName = decisionGroupRepository.findByName(group.getName());
        if (byName != null && byName != group) {
            return ResultType.NAME_FOUND;
        }
        decisionGroupRepository.save(group);
        return ResultType.SUCCESS;
    }

    public enum ResultType {
        NAME_FOUND, SUCCESS
    }

    public record DecisionResult(ResultType result, QuizDecision decision) {
    }

}
