package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.LOGIN_EXCEPTION;
import static com.example.servingwebcontent.consts.MessageConsts.LOGIN_LOGOUT_EXCEPTION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_GetLogin_Test extends AuthControllerTest {

	private final static String URL = "/login";

	@Test
	void whenAnonymousGetLoginThenSuccess() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"));
	}

	@Test
	void whenAnonymousGetLoginWithErrorThenSuccess() throws Exception {
		mockMvc.perform(get(URL)
				.param("error", new String[]{null})
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"))
			.andExpect(model().attribute("repairPassword", true))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, LOGIN_EXCEPTION));
	}

	@Test
	void whenAnonymousGetLoginWithLogoutThenSuccess() throws Exception {
		mockMvc.perform(get(URL)
				.param("logout", new String[]{null})
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, LOGIN_LOGOUT_EXCEPTION));
	}

	@Test
	void whenUserGetLoginThenForbidden() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}

}
