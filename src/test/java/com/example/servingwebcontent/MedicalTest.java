package com.example.servingwebcontent;


import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.repositories.medical.MedicalTaskRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTaskResultRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicRepository;
import com.example.servingwebcontent.repositories.medical.MedicalTopicResultRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@WithUserDetails("admin")
@Sql(value = {"/create-user-before.sql", "/create-medical-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-medical-after.sql", "/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MedicalTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalTopicRepository medicalTopicRepository;
    @Autowired
    private MedicalTaskRepository medicalTaskRepository;
    @Autowired
    private MedicalTopicResultRepository medicalTopicResultRepository;
    @Autowired
    private MedicalTaskResultRepository medicalTaskResultRepository;

    @Test
    public void medicalTopicList() throws Exception {
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(2))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 1']/td[1]").string("topic 1"))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 2']/td[1]").string("topic 2"))
        ;
        this.mockMvc.perform(get("/medical/1"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr").nodeCount(2))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-1']/td[1]").string("task-1 - ml/L"))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-2']/td[1]").string("task-2"))
        ;
        this.mockMvc.perform(get("/medical/2"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr").nodeCount(1))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-1']/td[1]").string("task-1"))
        ;
    }

    @Test
    public void createMedicalTopic() throws Exception {
        final String topic1 = "topic-1";
        final MedicalTopic medicalTopic1 = addTopicSuccess(topic1);
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
        ;
        this.mockMvc.perform(get("/medical/" + medicalTopic1.getId()))
            .andExpect(status().isOk())
        ;
    }

    @Test
    public void createMedicalTopicWithTrim() throws Exception {
        final String topic1 = "topic-1";
        addTopicSuccess(" " + topic1 + "  ");
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
        ;
    }

    @Test
    public void createMedicalTopicFailedEmptyField() throws Exception {
        final String topic1 = "";
        addTopicFailed(topic1);
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(2))
        ;
    }

    @Test
    public void createMedicalTopicFailedDuplicateName() throws Exception {
        final String topic1 = "topic-1";
        addTopicSuccess(topic1);
        addTopicFailed(topic1);
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
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
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(4))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic1 + "']/td[1]").string(topic1))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topic2 + "']/td[1]").string(topic2))
        ;
    }

    @Test
    public void renameTopicSuccess() throws Exception {
        final String topicName = "topic";
        final String topicName1 = "topic-1";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        this.mockMvc.perform(post("/medical/rename/" + medicalTopic.getId()).param("name", topicName1).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
            .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName1 + "']/td[1]").string(topicName1))
        ;
        assertEquals(3, medicalTopicRepository.count());
    }

    @Test
    public void renameTopicFailedEmptyName() throws Exception {
        final String topicName = "topic";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        this.mockMvc.perform(post("/medical/rename/" + medicalTopic.getId()).param("name", "").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
            .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName + "']/td[1]").string(topicName))
        ;
    }

    @Test
    public void renameTopicFailedDuplicateName() throws Exception {
        final String topicName = "topic";
        final String topicName1 = "topic-1";
        final MedicalTopic medicalTopic = addTopicSuccess(topicName);
        addTopicSuccess(topicName1);
        this.mockMvc.perform(post("/medical/rename/" + medicalTopic.getId()).param("name", topicName1).with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/" + medicalTopic.getId()))
            .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(4))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName + "']/td[1]").string(topicName))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='" + topicName1 + "']/td[1]").string(topicName1))
        ;
    }

    @Test
    public void deleteTopic() throws Exception {
        this.mockMvc.perform(post("/medical/delete/1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/list"))
            .andExpect(flash().attribute("successMessage", notNullValue()))
        ;
        this.mockMvc.perform(get("/medical/list"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(1))
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 1']/td[1]").doesNotExist())
            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 2']/td[1]").exists())
        ;
        assertEquals(1, medicalTopicRepository.count());
        assertEquals(1, medicalTaskRepository.count());
    }

    @Test
    public void copyTopic() throws Exception {
//        this.mockMvc.perform(post("/medical/copy/1").with(csrf()))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/medical/list"))
//            .andExpect(flash().attribute("successMessage", notNullValue()))
//        ;
//        this.mockMvc.perform(get("/medical/list"))
//            .andExpect(status().isOk())
//            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr").nodeCount(3))
//            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 1']/td[1]").string("topic 1"))
//            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 2']/td[1]").string("topic 2"))
//            .andExpect(xpath("//*[@id='medical-topic-table']/tbody/tr[@data-id='topic 1 - копия 1']/td[1]").string("topic 1 - копия 1"))
//        ;
//        final MedicalTopic copyTopic = medicalTopicRepository.findByName("topic 1 - копия 1");
//        this.mockMvc.perform(get("/medical/" + copyTopic.getId().toString()))
//            .andExpect(status().isOk())
//            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr").nodeCount(2))
//            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-1']/td[1]").string("task-1 - ml/L"))
//            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-2']/td[1]").string("task-2"))
//        ;
//        assertEquals(3, medicalTopicRepository.count());
//        assertEquals(5, medicalTaskRepository.count());
    }

    @Test
    public void topicResultAdd() throws Exception {
//        this.mockMvc.perform(post("/result/topic/1/newTopic/1").with(csrf()))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/result/topic/1"))
//            .andExpect(flash().attribute("successMessage", notNullValue()))
//        ;
//        this.mockMvc.perform(get("/userTopicList"))
//            .andExpect(status().isOk())
//            .andExpect(xpath("//*[@id='topic-invoke-table']/tbody/tr").nodeCount(1))
//            .andExpect(xpath("//*[@id='topic-invoke-table']/tbody/tr[@data-id='topic 1']").exists())
//        ;
//        final List<MedicalTopicResult> topicResults = medicalTopicResultRepository.findAllByMedicalTopic(medicalTopicRepository.findByName("topic 1"));
//        assertEquals(1, topicResults.size());
//        this.mockMvc.perform(get("/invokeTopic/" + topicResults.get(0).getId()))
//            .andExpect(status().isOk())
//            .andExpect(xpath("//*[@id='medical-topic-result-form']/div").nodeCount(3))
//            .andExpect(xpath("//*[@id='medical-topic-result-form']/div[@data-id='task-1']").exists())
//            .andExpect(xpath("//*[@id='medical-topic-result-form']/div[@data-id='task-2']").exists())
//        ;
//        assertEquals(2, medicalTaskResultRepository.count());
    }

    @Test
    public void topicResultDelete() throws Exception {
//        this.mockMvc.perform(post("/result/topic/1/newTopic/1").with(csrf()))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/result/topic/1"))
//            .andExpect(flash().attribute("successMessage", notNullValue()))
//        ;
//        final List<MedicalTopicResult> topicResults = medicalTopicResultRepository.findAllByMedicalTopic(medicalTopicRepository.findByName("topic 1"));
//        assertEquals(1, topicResults.size());
//        this.mockMvc.perform(post("/result/topic/1/delete/" + topicResults.get(0).getId()).with(csrf()))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/result/topic/1"))
//            .andExpect(flash().attribute("successMessage", notNullValue()))
//        ;
//        assertEquals(0, medicalTopicResultRepository.count());
//        assertEquals(0, medicalTaskResultRepository.count());
//        assertEquals(2, medicalTopicRepository.count());
//        this.mockMvc.perform(get("/userTopicList"))
//            .andExpect(status().isOk())
//            .andExpect(xpath("//*[@id='topic-invoke-table']/tbody/tr").nodeCount(0))
//            .andExpect(xpath("//*[@id='topic-invoke-table']/tbody/tr[@data-id='topic 1']").doesNotExist())
//        ;
    }

    @Test
    public void topicResultDeleteTopic() throws Exception {
        this.mockMvc.perform(post("/result/topic/1/newTopic/1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/result/topic/1"))
            .andExpect(flash().attribute("successMessage", notNullValue()))
        ;
        this.mockMvc.perform(post("/medical/delete/1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/list"))
            .andExpect(flash().attribute("successMessage", notNullValue()))
        ;
        assertEquals(0, medicalTopicResultRepository.count());
        assertEquals(0, medicalTaskResultRepository.count());
        assertEquals(1, medicalTopicRepository.count());
    }

    @Test
    public void addTask() throws Exception {
        final String topicName = "topic";
        final MedicalTopic topic = addTopicSuccess(topicName);
        this.mockMvc.perform(get("/medical/" + topic.getId()))
            .andExpect(status().isOk())
        ;
        addTaskSuccess("task-1", String.valueOf(topic.getId()), "m/s", "1", "1.4", "1.43", "2");
    }

    @Test
    public void addTask2() throws Exception {
        addTaskSuccess("task-2", "2", "", "1", "2", "3", "4");
    }

    @Test
    public void addTaskFailedSameName() throws Exception {
        addTaskFailed("task-1", "1", "", "1", "2", "3", "4");
    }

    @Test
    public void addTaskFailed() throws Exception {
        addTaskSuccess("task-3", "1", "l", "1", "2", "3", "4");
        addTaskFailed("task-4", "1", "", "2", "2", "2", "2");
        addTaskFailed("task-4", "1", "", "4", "3", "2", "1");
        addTaskFailed("task-4", "1", "", "4", "4", "5", "5");
        addTaskFailed("task-4", "1", "", "1", "", "3", "5");

        this.mockMvc.perform(get("/medical/1"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr").nodeCount(3))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-1']/td[1]").string("task-1 - ml/L"))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-2']/td[1]").string("task-2"))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-3']/td[1]").string("task-3 - l"))
        ;
    }

    @Test
    public void updateTask() throws Exception {
        this.mockMvc.perform(get("/medical/1/task/update/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/1"))
            .andExpect(flash().attribute("task", notNullValue()))
        ;
        this.mockMvc.perform(post("/medical/1/task/update").with(csrf())
                .param("id", "1")
                .param("name", "task-3")
                .param("leftLeft", "1")
                .param("leftMid", "2")
                .param("rightMid", "3")
                .param("rightRight", "4")
                .param("leftDecisions", "1", "2")
                .param("rightDecisions", "1", "6")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(flash().attribute("successMessage", notNullValue()));
    }

    @Test
    public void deleteTask() throws Exception {
        this.mockMvc.perform(post("/medical/1/task/delete/1").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/medical/1"))
            .andExpect(flash().attribute("successMessage", notNullValue()))
        ;
        this.mockMvc.perform(get("/medical/1"))
            .andExpect(status().isOk())
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr").nodeCount(1))
            .andExpect(xpath("//*[@id='medical-topic-task-table']/tbody/tr[@data-id='task-2']/td[1]").string("task-2"))
        ;
        assertEquals(2, medicalTaskRepository.count());
    }

    private MedicalTopic addTopicSuccess(String name) throws Exception {
//        this.mockMvc.perform(post("/medical/add").param("name", name).with(csrf()))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/medical/list"))
//            .andExpect(flash().attribute("successMessage", notNullValue()))
//            .andExpect(flash().attribute("message", nullValue()))
//        ;
//        return medicalTopicRepository.findByName(name);
        return null;
    }

    private void addTopicFailed(String name) throws Exception {
        this.mockMvc.perform(post("/medical/add").param("name", name).with(csrf()))
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
