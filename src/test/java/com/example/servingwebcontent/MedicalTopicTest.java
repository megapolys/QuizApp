package com.example.servingwebcontent;


import com.example.servingwebcontent.domain.medical.MedicalTopic;
import com.example.servingwebcontent.repositories.medical.MedicalTopicRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@WithUserDetails("admin")
@Sql(value = {"/create-user-before.sql", "/create-medical-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-medical-after.sql", "/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MedicalTopicTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalTopicRepository medicalTopicRepository;

    @Test
    public void createMedicalTopic() throws Exception {
        final String topic1 = "topic-1";
        final MedicalTopic medicalTopic1 = addTopicSuccess(topic1);
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
        ;
        this.mockMvc.perform(get("/medical/" + medicalTopic1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void createMedicalTopicFailedEmptyField() throws Exception {
        final String topic1 = "";
        addTopicFailed(topic1);
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void createMedicalTopicFailedDuplicateName() throws Exception {
        final String topic1 = "topic-1";
        addTopicSuccess(topic1);
        addTopicFailed(topic1);
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
        ;
    }

    @Test
    public void createTwoMedicalTopic() throws Exception {
        final String topic1 = "topic-1";
        final String topic2 = "topic-2";
        addTopicSuccess(topic1);
        addTopicSuccess(topic2);
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic2 + "']/td[1]").string(topic2))
        ;
    }

    @Test
    public void renameTopicSuccess() throws Exception {
        final String topicName = "topic";
        final String topicName1 = "topic-1";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        this.mockMvc.perform(get("/medical/rename/" + medicalTopic.getId()).param("name", topicName1))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName1 + "']/td[1]").string(topicName1))
        ;
        assertEquals(1, medicalTopicRepository.findAllByOrderByName().size());
    }

    @Test
    public void renameTopicFailedEmptyName() throws Exception {
        final String topicName = "topic";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        this.mockMvc.perform(get("/medical/rename/" + medicalTopic.getId()).param("name", ""))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName + "']/td[1]").string(topicName))
        ;
    }

    @Test
    public void renameTopicFailedDuplicateName() throws Exception {
        final String topicName = "topic";
        final String topicName1 = "topic-1";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        addTopicSuccess(topicName1);
        this.mockMvc.perform(get("/medical/rename/" + medicalTopic.getId()).param("name", topicName1))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName + "']/td[1]").string(topicName))
                .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName1 + "']/td[1]").string(topicName1))
        ;
    }

    @Test
    public void addTask() throws Exception {
        final String topicName = "topic";
        final MedicalTopic topic = addTopicSuccess(topicName);
        this.mockMvc.perform(get("/medical/" + topic.getId()))
                .andExpect(status().isOk())
        ;
        addTaskSuccess("task-1", String.valueOf(topic.getId()), "", "1", "1.4", "1.43", "2");
    }

    private MedicalTopic addTopicSuccess(String name) throws Exception {
        this.mockMvc.perform(get("/medical/add").param("name", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/list"))
                .andExpect(flash().attribute("successMessage", notNullValue()))
                .andExpect(flash().attribute("message", nullValue()))
        ;
        return medicalTopicRepository.findByName(name);
    }

    private void addTopicFailed(String name) throws Exception {
        this.mockMvc.perform(get("/medical/add").param("name", name))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/list"))
                .andExpect(flash().attribute("message", notNullValue()))
                .andExpect(flash().attribute("successMessage", nullValue()))
        ;
    }

    private void addTaskSuccess(String name, String topic, String unit, String leftLeft, String leftMid, String rightMid, String rightRight) throws Exception {
        this.mockMvc.perform(post("/medical/" + topic + "/task/add").with(csrf())
                        .param("name", name)
                        .param("unit", unit)
                        .param("leftLeft", leftLeft)
                        .param("leftMid", leftMid)
                        .param("rightMid", rightMid)
                        .param("rightRight", rightRight)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/" + topic))
                .andExpect(flash().attribute("successMessage", notNullValue()))
                .andExpect(flash().attribute("message", nullValue()))
        ;
    }

    private void addTaskFailed(String name, String topic, String unit, String leftLeft, String leftMid, String rightMid, String rightRight) throws Exception {
        this.mockMvc.perform(post("/medical/" + topic + "/task/add").with(csrf())
                        .param("name", name)
                        .param("unit", unit)
                        .param("leftLeft", leftLeft)
                        .param("leftMid", leftMid)
                        .param("rightMid", rightMid)
                        .param("rightRight", rightRight)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medical/" + topic))
                .andExpect(flash().attribute("message", notNullValue()))
                .andExpect(flash().attribute("successMessage", nullValue()))
        ;
    }
}
