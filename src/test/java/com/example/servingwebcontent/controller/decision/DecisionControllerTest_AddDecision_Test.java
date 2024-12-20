package com.example.servingwebcontent.controller.decision;

import com.example.servingwebcontent.generator.decision.DecisionEntityGenerator;
import com.example.servingwebcontent.generator.decision.DecisionWithGroupGenerator;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
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

public class DecisionControllerTest_AddDecision_Test extends DecisionControllerTest {

	private final static String URL = "/decisions";
	private final static String NAME_PARAM = "name";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenAddDecisionThenRedirect() throws Exception {
		DecisionWithGroup decision = DecisionWithGroupGenerator.generateNew();

		when(decisionRepository.existsByName(decision.getName())).thenReturn(false);

		mockMvc.perform(post(URL).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(decision)))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_ADDED));

		verify(decisionRepository).existsByName(anyString());
		ArgumentCaptor<DecisionEntity> decisionEntityArgumentCaptor = ArgumentCaptor.forClass(DecisionEntity.class);
		verify(decisionRepository).save(decisionEntityArgumentCaptor.capture());
		DecisionEntity actualDecisionEntity = decisionEntityArgumentCaptor.getValue();
		DecisionEntity expectedDecisionEntity = DecisionEntityGenerator.generateNew();
		then(actualDecisionEntity).isNotNull();
		then(actualDecisionEntity).isEqualTo(expectedDecisionEntity);
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenAddDecisionWithEmptyNameThenRedirect() throws Exception {
		DecisionWithGroup decision = DecisionWithGroupGenerator.generateNew();
		decision.setName(null);

		mockMvc.perform(post(URL).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(decision)))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, DECISION_NAME_IS_EMPTY));

		verify(decisionRepository, never()).existsByName(anyString());
		verify(decisionRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenDecisionNameAlreadyExistsThenRedirect() throws Exception {
		DecisionWithGroup decision = DecisionWithGroupGenerator.generateNew();

		when(decisionRepository.existsByName(anyString())).thenReturn(true);

		mockMvc.perform(post(URL).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(decision)))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, DECISION_WITH_SAME_NAME_ALREADY_EXISTS));

		verify(decisionRepository).existsByName(anyString());
		verify(decisionRepository, never()).save(any());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).existsByName(anyString());
		verify(decisionRepository, never()).save(any());
	}

	@Test
	@WithMockUser
	void whenNotAdminRequestThenForbidden() throws Exception {
		mockMvc.perform(post(URL).with(csrf()))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).existsByName(anyString());
		verify(decisionRepository, never()).save(any());
	}
}
