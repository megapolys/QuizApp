package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionEntityGenerator;
import com.example.servingwebcontent.generator.decision.DecisionWithGroupGenerator;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DecisionControllerTest_GetDecisionUpdate_Test extends DecisionControllerTest {

	private final static String URL = "/decisions/updateAction/{decisionId}";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGetGroupUpdateThenRedirect() throws Exception {
		DecisionEntity decisionEntity = DecisionEntityGenerator.generate();

		when(decisionRepository.findById(decisionEntity.getId())).thenReturn(Optional.of(decisionEntity));

		mockMvc.perform(get(URL, decisionEntity.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute("changeDecision", DecisionWithGroupGenerator.generate()));

		verify(decisionRepository).findById(anyLong());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGroupNotFoundThenRedirect() throws Exception {
		when(decisionRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get(URL, 1L))
			.andExpect(status().isBadRequest());

		verify(decisionRepository).findById(anyLong());
	}

	@Test
	@WithMockUser
	void whenNotAdminThenForbidden() throws Exception {
		mockMvc.perform(get(URL, 1L))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).findById(anyLong());
	}
}
