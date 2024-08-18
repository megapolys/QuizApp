package com.example.servingwebcontent.controller.auth;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.model.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;
import java.util.UUID;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.ACTIVATION_CODE_NOT_FOUND;
import static com.example.servingwebcontent.consts.MessageConsts.PROFILE_SUCCESSFUL_ACTIVATED;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_GetActivateByCode_Test extends AuthControllerTest {

	private final static String URL = "/activate";

	@Test
	void whenActivateThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generateInactive();

		when(userRepository.findByActivationCode(userEntity.getActivationCode())).thenReturn(Optional.of(userEntity));

		mockMvc.perform(get(URL + "/" + userEntity.getActivationCode())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"))
			.andExpect(model().attribute(SUCCESS_MESSAGE_PARAM, PROFILE_SUCCESSFUL_ACTIVATED));

		verify(userRepository).findByActivationCode(anyString());

		ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
		verify(userRepository).save(userEntityArgumentCaptor.capture());
		then(userEntityArgumentCaptor).isNotNull()
			.extracting(ArgumentCaptor::getValue).isEqualTo(UserEntityGenerator.generate());
	}

	@Test
	void whenActivationCodeNotFoundThenSuccess() throws Exception {
		when(userRepository.findByActivationCode(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(get(URL + "/" + UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/login"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, ACTIVATION_CODE_NOT_FOUND));

		verify(userRepository).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenUserTryActivateThenForbidden() throws Exception {
		mockMvc.perform(get(URL + "/" + UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

}
