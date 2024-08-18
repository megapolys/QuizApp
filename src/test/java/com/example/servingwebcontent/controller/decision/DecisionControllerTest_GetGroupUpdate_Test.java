package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionGroupEntityGenerator;
import com.example.servingwebcontent.generator.decision.GroupGenerator;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DecisionControllerTest_GetGroupUpdate_Test extends DecisionControllerTest {

	private final static String URL = "/decisions/group/updateAction/{groupId}";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGetGroupUpdateThenRedirect() throws Exception {
		DecisionGroupEntity groupEntity = DecisionGroupEntityGenerator.generate();

		when(decisionGroupRepository.findById(groupEntity.getId())).thenReturn(Optional.of(groupEntity));

		mockMvc.perform(get(URL, groupEntity.getId()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute("changeGroup", GroupGenerator.generate()));

		verify(decisionGroupRepository).findById(anyLong());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGroupNotFoundThenRedirect() throws Exception {
		when(decisionGroupRepository.findById(1L)).thenReturn(Optional.empty());

		mockMvc.perform(get(URL, 1L))
			.andExpect(status().isBadRequest());

		verify(decisionGroupRepository).findById(anyLong());
	}

	@Test
	@WithMockUser
	void whenNotAdminThenForbidden() throws Exception {
		mockMvc.perform(get(URL, 1L))
			.andExpect(status().isForbidden());

		verify(decisionGroupRepository, never()).findById(anyLong());
	}
}
