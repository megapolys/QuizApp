package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.generator.quiz.QuizWithTaskSizeEntityGenerator;
import com.example.servingwebcontent.generator.quiz.QuizWithTaskSizeGenerator;
import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class QuizController_GetQuizList_Test extends QuizControllerTest {

	private final static String URL = "/quiz/list";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGetQuizListThenSuccess() throws Exception {
		List<QuizWithTaskSizeEntity> quizEntities = QuizWithTaskSizeEntityGenerator.generateList();
		List<QuizWithTaskSize> expectedQuizList = QuizWithTaskSizeGenerator.generateList();

		when(quizCustomRepository.getQuizListOrderedByShortName()).thenReturn(quizEntities);

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("quiz/quizList"))
			.andExpect(model().attribute("quizList", expectedQuizList))
			.andExpect(model().attribute("quizTab", "active"));

		verify(quizCustomRepository).getQuizListOrderedByShortName();
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGetEmptyQuizListThenSuccess() throws Exception {
		when(quizCustomRepository.getQuizListOrderedByShortName()).thenReturn(List.of());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("quiz/quizList"))
			.andExpect(model().attribute("quizList", List.of()))
			.andExpect(model().attribute("quizTab", "active"));

		verify(quizCustomRepository).getQuizListOrderedByShortName();
	}

	@Test
	@WithMockUser
	void whenNotAdminThenForbidden() throws Exception {
		mockMvc.perform(get(URL))
			.andExpect(status().isForbidden());

		verify(quizCustomRepository, never()).getQuizListOrderedByShortName();
	}

}
