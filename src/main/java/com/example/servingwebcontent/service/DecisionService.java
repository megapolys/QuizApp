package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.medical.MedicalTask;
import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.repositories.DecisionGroupRepository;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTaskRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DecisionService {

    private final DecisionRepository decisionRepository;
    private final DecisionGroupRepository decisionGroupRepository;
    private final QuizTaskRepository quizTaskRepository;
    private final MedicalTaskRepository medicalTaskRepository;

    public DecisionService(DecisionRepository decisionRepository, DecisionGroupRepository decisionGroupRepository, QuizTaskRepository quizTaskRepository, MedicalTaskRepository medicalTaskRepository) {
        this.decisionRepository = decisionRepository;
        this.decisionGroupRepository = decisionGroupRepository;
        this.quizTaskRepository = quizTaskRepository;
        this.medicalTaskRepository = medicalTaskRepository;
    }

    public List<QuizDecision> decisionsWithoutGroups() {
        return decisions().stream().filter((d) -> d.getGroup() == null).toList();
    }

    private List<QuizDecision> decisions() {
        return decisionRepository.findAllByOrderByName();
    }

    public List<DecisionGroup> groups() {
        final List<DecisionGroup> groups = decisionGroupRepository.findAllByOrderByName();
        for (DecisionGroup group : groups) {
            group.setDecisions(group.getDecisions().stream().sorted(Comparator.comparing(QuizDecision::getName)).collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return groups;
    }

    public ResultType add(QuizDecision decision) {
        final QuizDecision byName = decisionRepository.findByName(decision.getName());
        if (byName != null) {
            return ResultType.NAME_FOUND;
        }
        if (decision.getGroup() != null) {
            final DecisionGroup group = decisionGroupRepository.findById(decision.getGroup().getId()).orElseThrow(); // нужно для актуализации данных из бд
            group.getDecisions().add(decision);
        }
        decisionRepository.save(decision);
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

    @Transactional
    public void delete(QuizDecision decision) {
        for (QuizTask task : quizTaskRepository.findAll()) {
            if (task.getDecisions().remove(decision)) {
                quizTaskRepository.save(task);
            }
        }
        for (MedicalTask task : medicalTaskRepository.findAll()) {
            if (task.getLeftDecisions().remove(decision) | task.getRightDecisions().remove(decision)) {
                medicalTaskRepository.save(task);
            }
        }
        if (decision.getGroup() != null) {
            decision.getGroup().getDecisions().remove(decision);
            decisionGroupRepository.save(decision.getGroup());
        }
        decisionRepository.delete(decision);
    }

    public void delete(DecisionGroup group) {
        for (QuizDecision decision : decisionRepository.findAll()) {
            if (decision.getGroup() != null && Objects.equals(decision.getGroup().getId(), group.getId())) {
                decision.setGroup(null);
                decisionRepository.save(decision);
            }
        }
        decisionGroupRepository.delete(group);
    }

    @Transactional
    public ResultType updateDecision(QuizDecision decision, DecisionGroup oldGroup) {
        final QuizDecision byName = decisionRepository.findByName(decision.getName());
        if (byName != null && !Objects.equals(byName.getId(), decision.getId())) {
            return ResultType.NAME_FOUND;
        }
        if (oldGroup == null) {
            updateGroup(decision);
        }
        if (oldGroup != null && decision.getGroup() == null) {
            oldGroup.getDecisions().removeIf(d -> Objects.equals(d.getId(), decision.getId()));
            decisionGroupRepository.save(oldGroup);
        } else if (oldGroup != null && !Objects.equals(decision.getGroup().getId(), oldGroup.getId())) {
            updateGroup(decision);
            oldGroup.getDecisions().removeIf(d -> Objects.equals(d.getId(), decision.getId()));
            decisionGroupRepository.save(oldGroup);
        }
        decisionRepository.save(decision);
        return ResultType.SUCCESS;
    }

    private void updateGroup(QuizDecision decision) {
        if (decision.getGroup() != null) {
            final DecisionGroup group = decisionGroupRepository.findById(decision.getGroup().getId()).orElseThrow(); // нужно для актуализации данных из бд
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
