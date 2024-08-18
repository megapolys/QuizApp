package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.Application;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.custom.QuizCustomRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public abstract class QuizControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	protected QuizRepository quizRepository;

	@MockBean
	protected QuizCustomRepository quizCustomRepository;
}