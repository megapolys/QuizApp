package com.example.servingwebcontent;

import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource("/application-test.yml")
@Sql(value = {"/create-user-before.sql", "/create-decisions-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-decisions-after.sql", "/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DecisionsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void decisionPageTest() throws Exception {
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='navbarNav']/a").string("admin"));
    }

    @Test
    public void decisionCountTest() throws Exception {
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    @Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void decisionCountEmptyTest() throws Exception {
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionAddWithGroupTest() throws Exception {
        this.mockMvc.perform(get("/decisions/add")
                        .param("group", "1")
                        .param("name", "dec001")
                        .param("description", "description")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']/td[1]/a").string(containsString("dec001")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']/td[1]/div/div/div/div[2]").string(containsString("description")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionAddNoGroupNoDescriptionTest() throws Exception {
        this.mockMvc.perform(get("/decisions/add")
                        .param("group", "")
                        .param("name", "dec001")
                        .param("description", "")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(4))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']/td[1]/a").string(containsString("dec001")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']/td[1]/div/div/div/div[2]").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionAddInEmptyGroupTest() throws Exception {
        this.mockMvc.perform(get("/decisions/add")
                        .param("group", "3")
                        .param("name", "dec001")
                        .param("description", "description")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr[@data-id='dec001']").exists())
        ;
    }

    @Test
    public void decisionAddSameNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/add")
                        .param("group", "1")
                        .param("name", "dec1")
                        .param("description", "description")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionAddEmptyNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/add")
                        .param("group", "1")
                        .param("name", "")
                        .param("description", "description")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='']").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateGroupTest() throws Exception {
        this.mockMvc.perform(get("/decisions/update/1")
                        .param("group", "3")
                        .param("name", "dec1")
                        .param("description", "description1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/a").string(containsString("dec1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/div/div/div/div[2]").string(containsString("description1")))
        ;
    }

    @Test
    public void decisionUpdateNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/update/1")
                        .param("group", "")
                        .param("name", "dec1-3")
                        .param("description", "description1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']/td[1]/a").string(containsString("dec1-3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']/td[1]/div/div/div/div[2]").string(containsString("description1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateDescriptionTest() throws Exception {
        this.mockMvc.perform(get("/decisions/update/1")
                        .param("group", "")
                        .param("name", "dec1")
                        .param("description", "description1-3")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/a").string(containsString("dec1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/div/div/div/div[2]").string(containsString("description1-3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateNoChangeTest() throws Exception {
        this.mockMvc.perform(get("/decisions/update/1")
                        .param("group", "")
                        .param("name", "dec1")
                        .param("description", "description1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/a").string(containsString("dec1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']/td[1]/div/div/div/div[2]").string(containsString("description1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateAll1Test() throws Exception {
        this.mockMvc.perform(get("/decisions/update/1")
                        .param("group", "1")
                        .param("name", "dec2-2")
                        .param("description", "description2-2")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec2-2']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec2-2']/td[1]/a").string(containsString("dec2-2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec2-2']/td[1]/div/div/div/div[2]").string(containsString("description2-2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateAll6Test() throws Exception {
        this.mockMvc.perform(get("/decisions/update/6")
                        .param("group", "")
                        .param("name", "dec1-3")
                        .param("description", "description1-3")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(4))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']/td[1]/a").string(containsString("dec1-3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1-3']/td[1]/div/div/div/div[2]").string(containsString("description1-3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionUpdateAction1Test() throws Exception {
        this.mockMvc.perform(get("/decisions/updateAction/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("changeDecision", notNullValue(QuizDecision.class)))
        ;
    }

    @Test
    public void decisionDelete1Test() throws Exception {
        this.mockMvc.perform(get("/decisions/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec1']").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void decisionDelete4Test() throws Exception {
        this.mockMvc.perform(get("/decisions/delete/4"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr[@data-id='dec4']").doesNotExist())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupCountTest() throws Exception {
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupAddTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/add")
                        .param("name", "group")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(5))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
                .andExpect(xpath("//*[@id='group--45-decisions']").string(containsString("group")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group--45-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupAddSameNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/add")
                        .param("name", "group1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(4))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupAddEmptyNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/add")
                        .param("name", "")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(4))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupUpdateTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/update/1")
                        .param("name", "group1-1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(4))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1-1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupUpdateSameNameFailedTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/update/1")
                        .param("name", "group2")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("message", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(4))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupUpdateSameNameTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/update/1")
                        .param("name", "group1")
                )
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(4))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupUpdateActionTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/updateAction/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("changeGroup", notNullValue(DecisionGroup.class)))
        ;
    }

    @Test
    public void groupDelete1Test() throws Exception {
        this.mockMvc.perform(get("/decisions/group/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(3))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(5))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec4']").exists())
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr[@data-id='dec5']").exists())
                .andExpect(xpath("//*[@id='group-1-decisions']").doesNotExist())
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").string(containsString("group3")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-3-decisions']/div/div[2]/table/tbody/tr").nodeCount(0))
        ;
    }

    @Test
    public void groupDelete3Test() throws Exception {
        this.mockMvc.perform(get("/decisions/group/delete/3"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decisions"))
                .andExpect(flash().attribute("successMessage", notNullValue()));
        this.mockMvc.perform(get("/decisions"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//*[@id='accordion_groups']/div").nodeCount(3))
                .andExpect(xpath("//*[@id='group-nonGroup-decisions']").string(containsString("Не в группе")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-nonGroup-decisions']/div/div[2]/table/tbody/tr").nodeCount(3))
                .andExpect(xpath("//*[@id='group-1-decisions']").string(containsString("group1")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-1-decisions']/div/div[2]/table/tbody/tr").nodeCount(2))
                .andExpect(xpath("//*[@id='group-2-decisions']").string(containsString("group2")))
                .andExpect(xpath("//*[@id='panelsStayOpen-group-2-decisions']/div/div[2]/table/tbody/tr").nodeCount(1))
                .andExpect(xpath("//*[@id='group-3-decisions']").doesNotExist())
        ;
    }

    @Test
    public void groupDeleteNotExistsTest() throws Exception {
        this.mockMvc.perform(get("/decisions/group/delete/4"))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }
}
