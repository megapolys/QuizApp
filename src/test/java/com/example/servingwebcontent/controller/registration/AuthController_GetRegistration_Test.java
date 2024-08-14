package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AuthController_GetRegistration_Test extends AuthControllerTest {

	private final static String URL = "/registration";

	@Test
	void whenAnonymousTryRegistrationThenSuccess() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"));
	}

	@Test
	void whenUserTryRegistrationThenForbidden() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}

}
