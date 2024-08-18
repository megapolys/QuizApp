package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionEntityGenerator;
import com.example.servingwebcontent.generator.decision.DecisionGenerator;
import com.example.servingwebcontent.generator.decision.DecisionGroupEntityGenerator;
import com.example.servingwebcontent.generator.decision.GroupWithDecisionsGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DecisionControllerTest_GetDecisions_Test extends DecisionControllerTest {

	private final static String URL = "/decisions";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGetDecisionsThenSuccess() throws Exception {
		when(decisionRepository.findAllByGroupIdIsNullOrderByName()).thenReturn(DecisionEntityGenerator.generateUngroupedList());
		when(decisionRepository.findAllByGroupIdIsNotNullOrderByName()).thenReturn(DecisionEntityGenerator.generateList());
		when(decisionGroupRepository.findAllByOrderByName()).thenReturn(DecisionGroupEntityGenerator.generateList());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("decisions/decisions"))
			.andExpect(model().attribute("decisions", DecisionGenerator.generateList()))
			.andExpect(model().attribute("groups", GroupWithDecisionsGenerator.generateList()))
			.andExpect(model().attribute("decisionTab", "active"));

		verify(decisionRepository).findAllByGroupIdIsNullOrderByName();
		verify(decisionRepository).findAllByGroupIdIsNotNullOrderByName();
		verify(decisionGroupRepository).findAllByOrderByName();
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenDecisionsAndGroupsEmptyThenSuccess() throws Exception {
		when(decisionRepository.findAllByGroupIdIsNullOrderByName()).thenReturn(List.of());
		when(decisionRepository.findAllByGroupIdIsNotNullOrderByName()).thenReturn(List.of());
		when(decisionGroupRepository.findAllByOrderByName()).thenReturn(List.of());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("decisions/decisions"))
			.andExpect(model().attribute("decisions", List.of()))
			.andExpect(model().attribute("groups", List.of()))
			.andExpect(model().attribute("decisionTab", "active"));

		verify(decisionRepository).findAllByGroupIdIsNullOrderByName();
		verify(decisionRepository).findAllByGroupIdIsNotNullOrderByName();
		verify(decisionGroupRepository).findAllByOrderByName();
	}

	@Test
	@WithMockUser
	void whenNotAdminThenForbidden() throws Exception {
		mockMvc.perform(get(URL))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).findAllByGroupIdIsNullOrderByName();
		verify(decisionRepository, never()).findAllByGroupIdIsNotNullOrderByName();
		verify(decisionGroupRepository, never()).findAllByOrderByName();
	}
}
