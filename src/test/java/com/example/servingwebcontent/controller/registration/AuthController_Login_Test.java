package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.model.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthController_Login_Test extends AuthControllerTest {

	private final static String URL = "/login";
	private final static String USERNAME_PARAM = "username";
	private final static String PASSWORD_PARAM = "password";

	@Test
	void whenAnonymousTryLoginByUsernameThenSuccess() throws Exception {
		String password = "password";
		UserEntity userEntity = UserEntityGenerator.generate();

		when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
		when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(USERNAME_PARAM, userEntity.getUsername())
				.param(PASSWORD_PARAM, password)
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));

		verify(userRepository).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(passwordEncoder).matches(anyString(), anyString());
		verify(userRoleRepository).findAllByUserId(any());
		verify(roleRepository).findAllById(any());
	}

	@Test
	void whenAnonymousTryLoginByEmailThenSuccess() throws Exception {
		String password = "password";
		UserEntity userEntity = UserEntityGenerator.generate();

		when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
		when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(true);

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(USERNAME_PARAM, userEntity.getEmail())
				.param(PASSWORD_PARAM, password)
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/"));

		verify(userRepository).findByUsername(anyString());
		verify(userRepository).findByEmail(anyString());
		verify(passwordEncoder).matches(anyString(), anyString());
		verify(userRoleRepository).findAllByUserId(any());
		verify(roleRepository).findAllById(any());
	}

	@Test
	void whenNotFoundByLoginThenSuccess() throws Exception {
		String password = "password";
		UserEntity userEntity = UserEntityGenerator.generate();

		when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(USERNAME_PARAM, userEntity.getUsername())
				.param(PASSWORD_PARAM, password)
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"));

		verify(userRepository).findByUsername(anyString());
		verify(userRepository).findByEmail(anyString());
		verify(passwordEncoder, never()).matches(anyString(), anyString());
		verify(userRoleRepository, never()).findAllByUserId(any());
		verify(roleRepository, never()).findAllById(any());
	}

	@Test
	void whenWrongPasswordThenSuccess() throws Exception {
		String password = "password";
		UserEntity userEntity = UserEntityGenerator.generate();

		when(userRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());
		when(passwordEncoder.matches(password, userEntity.getPassword())).thenReturn(false);

		mockMvc.perform(post(URL)
				.with(csrf())
				.param(USERNAME_PARAM, userEntity.getUsername())
				.param(PASSWORD_PARAM, password)
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/login?error"));

		verify(userRepository).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(passwordEncoder).matches(anyString(), anyString());
		verify(userRoleRepository).findAllByUserId(any());
		verify(roleRepository).findAllById(any());
	}

	@Test
	void whenAnonymousTryLoginWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenUserTryLoginThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}

}
