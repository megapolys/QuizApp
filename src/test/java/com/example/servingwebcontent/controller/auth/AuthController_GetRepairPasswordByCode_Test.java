package com.example.servingwebcontent.controller.auth;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.UUID;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.REPAIR_PASSWORD_CODE_NOT_FOUND;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_GetRepairPasswordByCode_Test extends AuthControllerTest {

	private final static String URL = "/repairPassword";

	@Test
	void whenGetRepairPasswordThenSuccess() throws Exception {
		when(userRepository.existsByRepairPasswordCode(anyString())).thenReturn(true);

		UUID repairPasswordCode = UUID.randomUUID();
		mockMvc.perform(get(URL + "/" + repairPasswordCode)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/repairPassword"))
			.andExpect(model().attribute("validCode", repairPasswordCode.toString()));

		verify(userRepository).existsByRepairPasswordCode(anyString());
	}

	@Test
	void whenRepairPasswordNotFoundThenSuccess() throws Exception {
		when(userRepository.existsByRepairPasswordCode(anyString())).thenReturn(false);

		mockMvc.perform(get(URL + "/" + UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_NOT_FOUND));

		verify(userRepository).existsByRepairPasswordCode(anyString());
	}

	@Test
	void whenUserGetRepairPasswordThenSuccess() throws Exception {
		mockMvc.perform(get(URL + "/" + UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}

}
