package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionGroupEntityGenerator;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.test.context.support.WithMockUser;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DecisionControllerTest_AddGroup_Test extends DecisionControllerTest {

	private final static String URL = "/decisions/group";
	private final static String NAME_PARAM = "name";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenAddGroupThenRedirect() throws Exception {
		DecisionGroupEntity expectedDecisionGroupEntity = DecisionGroupEntityGenerator.generateNew();
		String name = expectedDecisionGroupEntity.getName();

		when(decisionGroupRepository.existsByName(name)).thenReturn(false);

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(NAME_PARAM, " " + name + " ")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_ADDED));

		verify(decisionGroupRepository).existsByName(anyString());
		ArgumentCaptor<DecisionGroupEntity> decisionGroupEntityArgumentCaptor = ArgumentCaptor.forClass(DecisionGroupEntity.class);
		verify(decisionGroupRepository).save(decisionGroupEntityArgumentCaptor.capture());
		DecisionGroupEntity actualDecisionGroupEntity = decisionGroupEntityArgumentCaptor.getValue();
		then(actualDecisionGroupEntity).isNotNull();
		then(actualDecisionGroupEntity).isEqualTo(expectedDecisionGroupEntity);
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenAddGroupWithEmptyNameThenRedirect() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.param(NAME_PARAM, " ")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, GROUP_NAME_IS_EMPTY));

		verify(decisionGroupRepository, never()).existsByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGroupNameAlreadyExistsThenRedirect() throws Exception {
		when(decisionGroupRepository.existsByName(anyString())).thenReturn(true);

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(NAME_PARAM, "name")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, GROUP_WITH_SAME_NAME_ALREADY_EXISTS));

		verify(decisionGroupRepository).existsByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL))
			.andExpect(status().isForbidden());

		verify(decisionGroupRepository, never()).existsByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser
	void whenNotAdminRequestThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.param(NAME_PARAM, "name")
			)
			.andExpect(status().isForbidden());

		verify(decisionGroupRepository, never()).existsByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}
}
