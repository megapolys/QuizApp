package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.model.quiz.decision.QuizDecision;
import com.example.servingwebcontent.repositories.DecisionGroupRepository;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTaskRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
//        return decisionRepository.findAllByOrderByName();
		return null;
	}

    public List<DecisionGroup> groups() {
//        final List<DecisionGroup> groups = decisionGroupRepository.findAllByOrderByName();
//        for (DecisionGroup group : groups) {
//            group.setDecisions(group.getDecisions().stream().sorted(Comparator.comparing(QuizDecision::getName)).collect(Collectors.toCollection(LinkedHashSet::new)));
//        }
//        return groups;
		return null;
	}

    public ResultType add(QuizDecision decision) {
//        final QuizDecision byName = decisionRepository.findByName(decision.getName());
//        if (byName != null) {
//            return ResultType.NAME_FOUND;
//        }
//        decisionRepository.save(decision);
//        return ResultType.SUCCESS;
		return null;
	}

    public ResultType add(DecisionGroup group) {
//        final DecisionGroup byName = decisionGroupRepository.findByName(group.getName());
//        if (byName != null) {
//            return ResultType.NAME_FOUND;
//        }
//        decisionGroupRepository.save(group);
//        return ResultType.SUCCESS;
		return null;
	}

    @Transactional
    public void delete(QuizDecision decision) {
//        for (QuizTask task : quizTaskRepository.findAll()) {
//            if (task.getDecisions().remove(decision)) {
//                quizTaskRepository.save(task);
//            }
//        }
//        for (MedicalTask task : medicalTaskRepository.findAll()) {
//            if (task.getLeftDecisions().remove(decision) | task.getRightDecisions().remove(decision)) {
//                medicalTaskRepository.save(task);
//            }
//        }
//        decisionRepository.delete(decision);
	}

    @Transactional
    public void delete(DecisionGroup group) {
//        for (QuizDecision decision : decisionRepository.findAllByGroup(group)) {
//            decision.setGroup(null);
//            decisionRepository.save(decision);
//        }
//        decisionGroupRepository.delete(group);
	}

    @Transactional
    public ResultType update(QuizDecision decision) {
//        final QuizDecision byName = decisionRepository.findByName(decision.getName());
//        if (byName != null && !Objects.equals(byName.getId(), decision.getId())) {
//            return ResultType.NAME_FOUND;
//        }
//        decisionRepository.save(decision);
//        return ResultType.SUCCESS;
		return null;
	}

    @Transactional
    public ResultType update(DecisionGroup group) {
//        final DecisionGroup byName = decisionGroupRepository.findByName(group.getName());
//        if (byName != null && !Objects.equals(byName.getId(), group.getId())) {
//            return ResultType.NAME_FOUND;
//        }
//        decisionGroupRepository.save(group);
//        return ResultType.SUCCESS;
		return null;
	}

    public enum ResultType {
        NAME_FOUND, SUCCESS
    }

}
