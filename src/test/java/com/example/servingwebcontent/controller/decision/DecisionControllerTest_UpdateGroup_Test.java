package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionGroupEntityGenerator;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

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

public class DecisionControllerTest_UpdateGroup_Test extends DecisionControllerTest {

	private final static String URL = "/decisions/group/{groupId}";
	private final static String NAME_PARAM = "name";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenUpdateGroupThenRedirect() throws Exception {
		DecisionGroupEntity expectedDecisionGroupEntity = DecisionGroupEntityGenerator.generate();
		String newName = "new_group";

		when(decisionGroupRepository.findByName(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL, expectedDecisionGroupEntity.getId())
				.with(csrf())
				.param(NAME_PARAM, " " + newName + " ")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_CHANGED));

		verify(decisionGroupRepository).findByName(anyString());
		ArgumentCaptor<DecisionGroupEntity> decisionGroupEntityArgumentCaptor = ArgumentCaptor.forClass(DecisionGroupEntity.class);
		verify(decisionGroupRepository).save(decisionGroupEntityArgumentCaptor.capture());
		DecisionGroupEntity actualDecisionGroupEntity = decisionGroupEntityArgumentCaptor.getValue();
		then(actualDecisionGroupEntity).isNotNull();
		then(actualDecisionGroupEntity.getId()).isEqualTo(expectedDecisionGroupEntity.getId());
		then(actualDecisionGroupEntity.getName()).isEqualTo(newName);
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenUpdateGroupWithSameNameThenRedirect() throws Exception {
		DecisionGroupEntity expectedDecisionGroupEntity = DecisionGroupEntityGenerator.generate();
		String newName = expectedDecisionGroupEntity.getName();

		when(decisionGroupRepository.findByName(anyString())).thenReturn(Optional.of(expectedDecisionGroupEntity));

		mockMvc.perform(post(URL, expectedDecisionGroupEntity.getId())
				.with(csrf())
				.param(NAME_PARAM, " " + newName + " ")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_CHANGED));

		verify(decisionGroupRepository).findByName(anyString());
		ArgumentCaptor<DecisionGroupEntity> decisionGroupEntityArgumentCaptor = ArgumentCaptor.forClass(DecisionGroupEntity.class);
		verify(decisionGroupRepository).save(decisionGroupEntityArgumentCaptor.capture());
		DecisionGroupEntity actualDecisionGroupEntity = decisionGroupEntityArgumentCaptor.getValue();
		then(actualDecisionGroupEntity).isNotNull();
		then(actualDecisionGroupEntity.getId()).isEqualTo(expectedDecisionGroupEntity.getId());
		then(actualDecisionGroupEntity.getName()).isEqualTo(newName);
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenUpdateGroupWithEmptyNameThenRedirect() throws Exception {
		mockMvc.perform(post(URL, 1L)
				.with(csrf())
				.param(NAME_PARAM, " ")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, GROUP_NAME_IS_EMPTY));

		verify(decisionGroupRepository, never()).findByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenGroupNameAlreadyExistsThenRedirect() throws Exception {
		DecisionGroupEntity expectedDecisionGroupEntity = DecisionGroupEntityGenerator.generate();

		when(decisionGroupRepository.findByName(anyString())).thenReturn(Optional.of(expectedDecisionGroupEntity));

		mockMvc.perform(post(URL, 1L)
				.with(csrf())
				.param(NAME_PARAM, "name")
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, GROUP_WITH_SAME_NAME_ALREADY_EXISTS));

		verify(decisionGroupRepository).findByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL, 1L))
			.andExpect(status().isForbidden());

		verify(decisionGroupRepository, never()).findByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}

	@Test
	@WithMockUser
	void whenNotAdminRequestThenForbidden() throws Exception {
		mockMvc.perform(post(URL, 1L)
				.with(csrf())
				.param(NAME_PARAM, "name")
			)
			.andExpect(status().isForbidden());

		verify(decisionGroupRepository, never()).findByName(anyString());
		verify(decisionGroupRepository, never()).save(any());
	}
}
