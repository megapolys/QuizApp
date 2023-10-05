package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
        assertFalse(decisionGroupRepository.findAll().iterator().hasNext());
        final DecisionGroup group = new DecisionGroup();
        final DecisionService.ResultType resultType = decisionService.add(group);
        assertEquals(DecisionService.ResultType.SUCCESS, resultType);
        Mockito.verify(decisionGroupRepository, Mockito.times(1)).save(group);
    }

    @Test
    void addGroupFailed() {
        assertFalse(decisionGroupRepository.findAll().iterator().hasNext());
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
    void testAdd() {
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void updateDecision() {
    }

    @Test
    void updateGroup() {
    }
}