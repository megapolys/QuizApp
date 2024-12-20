package com.example.servingwebcontent.controller.auth;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AuthController_GetActivate_Test extends AuthControllerTest {

	private final static String URL = "/activate";

	@Test
	void whenAnonymousGetActivateThenSuccess() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/activation"));
	}

	@Test
	void whenUserGetActivateThenForbidden() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}
}
