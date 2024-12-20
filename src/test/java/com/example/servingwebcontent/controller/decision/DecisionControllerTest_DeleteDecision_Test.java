package com.example.servingwebcontent.controller.decision;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.DECISION_SUCCESSFUL_DELETED;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DecisionControllerTest_DeleteDecision_Test extends DecisionControllerTest {

	private final static String URL = "/decisions/{groupId}";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenDeleteDecisionThenRedirect() throws Exception {
		mockMvc.perform(delete(URL, 1L).with(csrf()))
			.andDo(print())
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/decisions"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_DELETED));

		verify(decisionRepository).deleteById(anyLong());
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(delete(URL, 1L))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).deleteById(anyLong());
	}

	@Test
	@WithMockUser
	void whenNotAdminRequestThenForbidden() throws Exception {
		mockMvc.perform(delete(URL, 1L).with(csrf()))
			.andExpect(status().isForbidden());

		verify(decisionRepository, never()).deleteById(anyLong());
	}
}
