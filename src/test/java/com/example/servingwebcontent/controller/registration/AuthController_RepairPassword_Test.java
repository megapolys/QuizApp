package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.model.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_RepairPassword_Test extends AuthControllerTest {

	private final static String URL = "/repairPassword";
	private final static String EMAIL_PARAM = "email";

	@Test
	void whenUserWithEmailFoundThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();

		when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(EMAIL_PARAM, userEntity.getEmail())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/repairPassword"))
			.andExpect(model().attribute(SUCCESS_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_SENT));

		verify(userRepository).findByEmail(anyString());

		ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
		verify(userRepository).save(userEntityArgumentCaptor.capture());
		then(userEntityArgumentCaptor).isNotNull();
		UserEntity actualUserEntity = userEntityArgumentCaptor.getValue();
		then(actualUserEntity.getId()).isEqualTo(userEntity.getId());
		then(actualUserEntity.getUsername()).isEqualTo(userEntity.getUsername());
		then(actualUserEntity.getPassword()).isEqualTo(userEntity.getPassword());
		then(actualUserEntity.getLastName()).isEqualTo(userEntity.getLastName());
		then(actualUserEntity.getFirstName()).isEqualTo(userEntity.getFirstName());
		then(actualUserEntity.getBirthday()).isEqualTo(userEntity.getBirthday());
		then(actualUserEntity.getEmail()).isEqualTo(userEntity.getEmail());
		then(actualUserEntity.isActive()).isEqualTo(userEntity.isActive());
		then(actualUserEntity.isMale()).isEqualTo(userEntity.isMale());
		then(actualUserEntity.getActivationCode()).isEqualTo(userEntity.getActivationCode());
		then(actualUserEntity.getRepairPasswordCode()).isNotNull();
	}

	@Test
	void whenEmailIsEmptyThenSuccess() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.param(EMAIL_PARAM, " ")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/repairPassword"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, EMAIL_REQUIRED));

		verify(userRepository, never()).findByEmail(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenEmailNotFoundThenSuccess() throws Exception {
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(EMAIL_PARAM, "email")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isOk())
			.andExpect(view().name("auth/repairPassword"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, PROFILE_BY_EMAIL_NOT_FOUND));

		verify(userRepository).findByEmail(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenRepairPasswordWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.param(EMAIL_PARAM, "email")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenUserTryRepairPasswordThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.param(EMAIL_PARAM, "email")
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

}
