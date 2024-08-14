package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.model.quiz.decision.QuizDecision;
import com.example.servingwebcontent.repositories.DecisionGroupRepository;
import com.example.servingwebcontent.repositories.DecisionRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTaskRepository;
import com.example.servingwebcontent.repositories.quiz.QuizTaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("/application-test.yml")
class DecisionServiceTest {

    @Autowired
    private DecisionService decisionService;

    @MockBean
    private DecisionRepository decisionRepository;
    @MockBean
    private DecisionGroupRepository decisionGroupRepository;
    @MockBean
    private QuizTaskRepository quizTaskRepository;
    @MockBean
    private MedicalTaskRepository medicalTaskRepository;

    @Test
    void addGroup() {
        final DecisionGroup group = new DecisionGroup();
        final DecisionService.ResultType resultType = decisionService.add(group);
        assertEquals(DecisionService.ResultType.SUCCESS, resultType);
        Mockito.verify(decisionGroupRepository, Mockito.times(1)).save(group);
    }

    @Test
    void addGroupFailed() {
        final DecisionGroup group = new DecisionGroup();
        group.setName("group");
        final DecisionGroup toBeReturned = new DecisionGroup();
        toBeReturned.setName("group");
        Mockito.doReturn(toBeReturned)
                .when(decisionGroupRepository)
                .findByName("group");
        final DecisionService.ResultType resultType = decisionService.add(group);
        assertEquals(DecisionService.ResultType.NAME_FOUND, resultType);
        Mockito.verify(decisionGroupRepository, Mockito.times(0)).save(any());
    }

    @Test
    void updateGroup() {
        final DecisionGroup group = new DecisionGroup();
        final DecisionService.ResultType resultType = decisionService.update(group);
        assertEquals(DecisionService.ResultType.SUCCESS, resultType);
        Mockito.verify(decisionGroupRepository, Mockito.times(1)).save(group);
    }

    @Test
    void updateGroupFailed() {
        final DecisionGroup group = new DecisionGroup();
        group.setName("group");
        final DecisionGroup toBeReturned = new DecisionGroup();
        toBeReturned.setName("group");
        toBeReturned.setId(1L);
        Mockito.doReturn(toBeReturned)
                .when(decisionGroupRepository)
                .findByName("group");
        final DecisionService.ResultType resultType = decisionService.update(group);
        assertEquals(DecisionService.ResultType.NAME_FOUND, resultType);
        Mockito.verify(decisionGroupRepository, Mockito.times(0)).save(any());
    }

    @Test
    void deleteGroup() {
        final DecisionGroup group = new DecisionGroup();
        group.setId(1L);
        decisionService.add(group);
        final QuizDecision dec1 = new QuizDecision();
        dec1.setGroup(group);
        decisionService.add(dec1);
        final QuizDecision dec2 = new QuizDecision();
        dec2.setGroup(group);
        decisionService.add(dec2);
        Mockito.doReturn(List.of(dec1, dec2))
                .when(decisionRepository)
                .findAllByGroup(group);
        decisionService.delete(dec1.getGroup());
        Mockito.verify(decisionGroupRepository, Mockito.times(1)).delete(group);
        assertNull(dec1.getGroup());
        assertNull(dec2.getGroup());

    }

    @Test
    void addDecision() {
        final QuizDecision decision = new QuizDecision();
        final DecisionService.ResultType resultType = decisionService.add(decision);
        assertEquals(DecisionService.ResultType.SUCCESS, resultType);
        Mockito.verify(decisionRepository, Mockito.times(1)).save(decision);
    }

    @Test
    void addDecisionFailed() {
        final QuizDecision decision = new QuizDecision();
        decision.setName("dec");
        final QuizDecision toBeReturned = new QuizDecision();
        toBeReturned.setName("dec");
        Mockito.doReturn(toBeReturned)
                .when(decisionRepository)
                .findByName("dec");
        final DecisionService.ResultType resultType = decisionService.add(decision);
        assertEquals(DecisionService.ResultType.NAME_FOUND, resultType);
        Mockito.verify(decisionRepository, Mockito.times(0)).save(any());
    }
    
    @Test
    void updateDecision() {
        final QuizDecision decision = new QuizDecision();
        final DecisionService.ResultType resultType = decisionService.update(decision);
        assertEquals(DecisionService.ResultType.SUCCESS, resultType);
        Mockito.verify(decisionRepository, Mockito.times(1)).save(decision);
    }

    @Test
    void updateDecisionFailed() {
        final QuizDecision decision = new QuizDecision();
        decision.setName("dec");
        final QuizDecision toBeReturned = new QuizDecision();
        toBeReturned.setId(1L);
        toBeReturned.setName("dec");
        Mockito.doReturn(toBeReturned)
                .when(decisionRepository)
                .findByName("dec");
        final DecisionService.ResultType resultType = decisionService.update(decision);
        assertEquals(DecisionService.ResultType.NAME_FOUND, resultType);
        Mockito.verify(decisionRepository, Mockito.times(0)).save(any());
    }

    @Test
    void deleteDecision() {
        final QuizDecision decision = new QuizDecision();
        decisionService.delete(decision);
        Mockito.verify(decisionRepository, Mockito.times(1)).delete(decision);
    }
}