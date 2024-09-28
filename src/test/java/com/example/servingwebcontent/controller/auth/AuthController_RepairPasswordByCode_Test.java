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
import static com.example.servingwebcontent.consts.MessageConsts.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_RepairPasswordByCode_Test extends AuthControllerTest {

	private final static String URL = "/repairPassword";
	private final static String PASSWORD_PARAM = "password";
	private final static String PASSWORD2_PARAM = "password2";

	@Test
	void whenUserWithEmailFoundThenRedirect() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generateWithRepairPasswordCode();
		String repairPasswordCode = UUID.randomUUID().toString();
		String newPassword = "password_new";

		when(userRepository.findByRepairPasswordCode(repairPasswordCode)).thenReturn(Optional.of(userEntity));
		when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, newPassword)
				.param(PASSWORD2_PARAM, newPassword)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, PASSWORD_SUCCESSFUL_CHANGED));

		verify(userRepository).findByRepairPasswordCode(anyString());
		verify(passwordEncoder).encode(anyString());

		ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
		verify(userRepository).save(userEntityArgumentCaptor.capture());
		then(userEntityArgumentCaptor).isNotNull();
		UserEntity actualUserEntity = userEntityArgumentCaptor.getValue();
		then(actualUserEntity.getId()).isEqualTo(userEntity.getId());
		then(actualUserEntity.getUsername()).isEqualTo(userEntity.getUsername());
		then(actualUserEntity.getPassword()).isEqualTo(newPassword);
		then(actualUserEntity.getLastName()).isEqualTo(userEntity.getLastName());
		then(actualUserEntity.getFirstName()).isEqualTo(userEntity.getFirstName());
		then(actualUserEntity.getBirthday()).isEqualTo(userEntity.getBirthday());
		then(actualUserEntity.getEmail()).isEqualTo(userEntity.getEmail());
		then(actualUserEntity.isActive()).isEqualTo(userEntity.isActive());
		then(actualUserEntity.getMale()).isEqualTo(userEntity.getMale());
		then(actualUserEntity.getActivationCode()).isEqualTo(userEntity.getActivationCode());
		then(actualUserEntity.getRepairPasswordCode()).isNull();
	}

	@Test
	void whenPasswordEmptyThenRedirect() throws Exception {
		String repairPasswordCode = UUID.randomUUID().toString();
		String newPassword = "password_new";

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, " ")
				.param(PASSWORD2_PARAM, newPassword)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/repairPassword/" + repairPasswordCode))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByRepairPasswordCode(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenPassword2EmptyThenRedirect() throws Exception {
		String repairPasswordCode = UUID.randomUUID().toString();
		String newPassword = "password_new";

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, newPassword)
				.param(PASSWORD2_PARAM, " ")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/repairPassword/" + repairPasswordCode))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByRepairPasswordCode(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenAllFieldsAreEmptyThenRedirect() throws Exception {
		String repairPasswordCode = UUID.randomUUID().toString();

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, "")
				.param(PASSWORD2_PARAM, " ")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/repairPassword/" + repairPasswordCode))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByRepairPasswordCode(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenDifferentPasswordsThenRedirect() throws Exception {
		String repairPasswordCode = UUID.randomUUID().toString();

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, "pass")
				.param(PASSWORD2_PARAM, "pass_other")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/repairPassword/" + repairPasswordCode))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, DIFFERENT_PASSWORDS));

		verify(userRepository, never()).findByRepairPasswordCode(anyString());
		verify(passwordEncoder, never()).encode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenRepairPasswordCodeNotFoundThenRedirect() throws Exception {
		String repairPasswordCode = UUID.randomUUID().toString();

		when(userRepository.findByRepairPasswordCode(repairPasswordCode)).thenReturn(Optional.empty());

		mockMvc.perform(post(URL + "/" + repairPasswordCode)
				.with(csrf())
				.param(PASSWORD_PARAM, "pass")
				.param(PASSWORD2_PARAM, "pass")
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login"))
			.andExpect(flash().attribute(ERROR_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_NOT_FOUND));

		verify(userRepository).findByRepairPasswordCode(anyString());
		verify(passwordEncoder).encode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenRepairPasswordWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL + "/" + UUID.randomUUID())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

	@Test
	void whenUserTryRepairPasswordThenForbidden() throws Exception {
		String newPassword = "password_new";

		mockMvc.perform(post(URL + "/" + UUID.randomUUID())
				.with(csrf())
				.param(PASSWORD_PARAM, newPassword)
				.param(PASSWORD2_PARAM, newPassword)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findByActivationCode(anyString());
		verify(userRepository, never()).save(any());
	}

}
