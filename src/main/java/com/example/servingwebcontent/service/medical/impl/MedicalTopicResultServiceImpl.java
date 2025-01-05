package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.medical.MedicalTask;
import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.model.medical.result.*;
import com.example.servingwebcontent.persistence.DecisionPersistence;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.property.MedicalScoreProperty;
import com.example.servingwebcontent.service.medical.MedicalTopicResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalTopicResultServiceImpl implements MedicalTopicResultService {

    private final MedicalPersistence medicalPersistence;
    private final DecisionPersistence decisionPersistence;
    private final MedicalScoreProperty property;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MedicalWithResults> getMedicalResultListByUserId(Long userId) {
        List<MedicalTopicResult> medicalResults = medicalPersistence.getMedicalResultListByUserId(userId);
        Map<Long, MedicalTopic> topicMapById = medicalPersistence.getAllTopics().stream()
            .collect(Collectors.toMap(MedicalTopic::getId, Function.identity()));
        return medicalResults.stream()
            .collect(Collectors.groupingBy(MedicalTopicResult::getTopicId))
            .entrySet().stream()
            .map(entry -> MedicalWithResults.builder()
                .topic(topicMapById.get(entry.getKey()))
                .results(entry.getValue().stream()
                    .sorted(Comparator.comparing(MedicalTopicResult::getCompleteDate, Comparator.nullsFirst(Comparator.reverseOrder())))
                    .map(this::getResult)
                    .toList()
                )
                .build())
            .sorted(Comparator.comparing((MedicalWithResults results) -> results.getTopic().getName()))
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMedicalResultById(Long medicalResultId) {
        medicalPersistence.deleteMedicalResultById(medicalResultId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MedicalFullResultDto getMedicalResultById(Long medicalResultId) {
        MedicalTopic medicalTopic = medicalPersistence.getMedicalTopicByTopicResultId(medicalResultId);
        List<MedicalTaskResultWithTask> taskResultList = medicalPersistence.getMedicalTaskResultByTopicResultId(medicalResultId);
        Map<Long, List<DecisionWithGroup>> leftDecisionWithGroupMap = decisionPersistence.findAllLeftDecisionsByMedicalTopicId(medicalTopic.getId()).stream()
            .collect(Collectors.groupingBy(DecisionWithGroup::getTaskId));
        Map<Long, List<DecisionWithGroup>> rightDecisionWithGroupMap = decisionPersistence.findAllRightDecisionsByMedicalTopicId(medicalTopic.getId()).stream()
            .collect(Collectors.groupingBy(DecisionWithGroup::getTaskId));

        List<MedicalTaskResultCalculated> results = new ArrayList<>();
        final Map<DecisionWithGroup, Float> decisionWeightMap = new LinkedHashMap<>();
        final Map<DecisionWithGroup, Integer> decisionCountMap = new LinkedHashMap<>();
        for (MedicalTaskResultWithTask taskResult : taskResultList) {
            final MedicalTask task = taskResult.getTask();

            List<DecisionWithGroup> decisions = new ArrayList<>();
            final float weight;
            if (taskResult.getValue() <= task.getLeftMid()) {
                if (leftDecisionWithGroupMap.containsKey(task.getId())) {
                    decisions = leftDecisionWithGroupMap.get(task.getId());
                }
                if (taskResult.getValue() <= task.getLeftLeft()) {
                    weight = property.getLeftLeft();
                } else {
                    weight = property.getLeftMid();
                }
            } else if (taskResult.getValue() >= task.getRightMid()) {
                if (rightDecisionWithGroupMap.containsKey(task.getId())) {
                    decisions = rightDecisionWithGroupMap.get(task.getId());
                }
                if (taskResult.getValue() >= task.getRightRight()) {
                    weight = property.getRightRight();
                } else {
                    weight = property.getRightMid();
                }
            } else {
                weight = 0;
            }

            for (DecisionWithGroup decision : decisions) {
                decisionWeightMap.compute(decision, (dec, localWeight) -> localWeight == null ? weight : localWeight + weight);
                decisionCountMap.compute(decision, (dec, count) -> count == null ? 1 : count + 1);
            }
            results.add(MedicalTaskResultCalculated.builder()
                .id(taskResult.getId())
                .task(taskResult.getTask())
                .topicResultId(taskResult.getTopicResultId())
                .value(taskResult.getValue())
                .score(weight)
                .decisions(decisions)
                .build());
        }
        results.sort(Comparator.comparing(taskResult -> taskResult.getTask().getId()));
        List<DecisionCalculated> decisions = decisionWeightMap.entrySet().stream()
            .map(entry -> DecisionCalculated.builder()
                .decision(entry.getKey())
                .score(entry.getValue())
                .count(decisionCountMap.get(entry.getKey()))
                .build())
            .sorted(Comparator.comparing(DecisionCalculated::getScore, Comparator.reverseOrder())
                .thenComparing(decisionCalculated -> decisionCalculated.getDecision().getName()))
            .toList();
        return MedicalFullResultDto.builder()
            .topicName(medicalTopic.getName())
            .results(results)
            .decisions(decisions)
            .build();
    }

    private MedicalResultCalculated getResult(MedicalTopicResult result) {
        List<MedicalTaskResultWithTask> taskResultList = medicalPersistence.getMedicalTaskResultByTopicResultId(result.getId());
        int taskCount = taskResultList.size();
        Long countCompleted = taskResultList.stream()
            .filter(taskResult -> taskResult.getValue() != null)
            .count();
        if (result.getCompleteDate() == null) {
            return MedicalResultCalculated.builder()
                .id(result.getId())
                .complete(false)
                .completeDate(null)
                .lastUpdateDate(result.getLastUpdateDate())
                .score(0)
                .taskCount(taskCount)
                .countCompleted(countCompleted)
                .build();
        }
        float weightSum = 0;
        for (MedicalTaskResultWithTask taskResult : taskResultList) {
            final MedicalTask task = taskResult.getTask();
            if (taskResult.getValue() == null) {
                continue;
            }

            final float weight;
            if (taskResult.getValue() <= task.getLeftMid()) {
                if (taskResult.getValue() <= task.getLeftLeft()) {
                    weight = property.getLeftLeft();
                } else {
                    weight = property.getLeftMid();
                }
            } else if (taskResult.getValue() >= task.getRightMid()) {
                if (taskResult.getValue() >= task.getRightRight()) {
                    weight = property.getRightRight();
                } else {
                    weight = property.getRightMid();
                }
            } else {
                weight = 0;
            }

            weightSum += weight;
        }
        final float score = weightSum / taskCount;
        return MedicalResultCalculated.builder()
            .id(result.getId())
            .complete(true)
            .completeDate(result.getCompleteDate())
            .lastUpdateDate(result.getLastUpdateDate())
            .score(score)
            .taskCount(taskCount)
            .countCompleted(countCompleted)
            .build();
    }

//    public AnalyseForm getAnalyse(MedicalTaskResult result) {
//        final float value = result.getValue();
//        final MedicalTask task = result.getMedicalTask();
//        final List<Float> values = new ArrayList<>();
//        final float gap = task.getRightRight() - task.getLeftLeft();
//        final float percent = 0.1f;
//        final float min = Math.min(value, task.getLeftLeft()) - gap * percent;
//        final float max = Math.max(value, task.getRightRight()) + gap * percent;
//        final float len = max - min;
//        values.add((task.getLeftLeft() - min) / len * 100);
//        values.add((task.getLeftMid() - task.getLeftLeft()) / len * 100);
//        values.add((task.getRightMid() - task.getLeftMid()) / len * 100);
//        values.add((task.getRightRight() - task.getRightMid()) / len * 100);
//        values.add((max - task.getRightRight()) / len * 100);
//        final float marker = (value - min) / len * 100;
//        return new AnalyseForm(values, marker);
//    }
}
