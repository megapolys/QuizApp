package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthController_Logout_Test extends AuthControllerTest {

	private final static String URL = "/logout";

	@Test
	void whenUserTryLogoutThenRedirect() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate()))
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?logout"));
	}

	@Test
	void whenUserTryLogoutWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate()))
			)
			.andExpect(status().isForbidden());
	}
}
