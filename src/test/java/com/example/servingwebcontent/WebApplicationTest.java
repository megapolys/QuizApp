package com.example.servingwebcontent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.yml")
@Sql(value = {"/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WebApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() throws Exception {
        for (String url : List.of("/", "/main")) {
            this.mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("Приветствую, посетитель!")));
        }
    }

    @Test
    public void loginTest() throws Exception {
        for (String url : List.of("/userQuizList", "/userTopicList", "/user/list", "/quiz/list", "/medicalList", "/decisions")) {
            this.mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }

    @Test
    public void loginAdmin() throws Exception {
        this.mockMvc.perform(formLogin().user("admin").password("adminPass"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithUserDetails("admin")
    public void adminTest() throws Exception {
        for (String url : List.of("/main", "/userQuizList", "/userTopicList", "/user/list", "/quiz/list", "/medical/list", "/decisions")) {
            this.mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @WithUserDetails("kiril")
    public void userTest() throws Exception {
        for (String url : List.of("/main", "/userQuizList", "/userTopicList")) {
            this.mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
        for (String url : List.of("/user/list", "/quiz/list", "/medical/list", "/decisions")) {
            this.mockMvc.perform(get(url))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    public void userLoginFailed() throws Exception {
        this.mockMvc.perform(post("/login").param("username", "kiril"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
