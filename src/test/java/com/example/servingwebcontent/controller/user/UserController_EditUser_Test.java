package com.example.servingwebcontent.controller.user;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.generator.user.UserSimpleGenerator;
import com.example.servingwebcontent.model.entities.UserEntity;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.model.user.UserSimple;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserController_EditUser_Test extends UserControllerTest {

	private final static String URL = "/user/edit";

	private final static String USERNAME_PARAM = "username";
	private final static String LAST_NAME_PARAM = "lastName";
	private final static String FIRST_NAME_PARAM = "firstName";
	private final static String EMAIL_PARAM = "email";
	private final static String BIRTHDAY_PARAM = "birthday";
	private final static String GENDER_PARAM = "male";

	@Test
	void whenNotChangedThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimple userSimple = UserSimpleGenerator.generate();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/user"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, "Изменения успешно сохранены"));

		verify(userRepository).findByUsername(userSimple.getUsername());
		verify(userRepository).findByEmail(userSimple.getEmail());
		verify(userRepository).findById(user.getId());
		verify(userRepository).save(userEntity);
	}

	@Test
	void whenChangedThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/user"))
			.andExpect(flash().attribute(SUCCESS_MESSAGE_PARAM, CHANGES_COMPLETE_SUCCESSFUL));

		verify(userRepository).findByUsername(userSimple.getUsername());
		verify(userRepository).findByEmail(userSimple.getEmail());
		verify(userRepository).findById(user.getId());
		verify(userRepository).save(changedUserEntity);
	}

	@Test
	void whenEmptyUsernameThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByUsername(userSimple.getUsername());
		verify(userRepository, never()).findByEmail(userSimple.getEmail());
		verify(userRepository, never()).findById(user.getId());
		verify(userRepository, never()).save(changedUserEntity);
	}

	@Test
	void whenEmptyLastNameThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByUsername(userSimple.getUsername());
		verify(userRepository, never()).findByEmail(userSimple.getEmail());
		verify(userRepository, never()).findById(user.getId());
		verify(userRepository, never()).save(changedUserEntity);
	}

	@Test
	void whenEmptyAllFieldsNameThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
			)
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(userRepository, never()).findByUsername(userSimple.getUsername());
		verify(userRepository, never()).findByEmail(userSimple.getEmail());
		verify(userRepository, never()).findById(user.getId());
		verify(userRepository, never()).save(changedUserEntity);
	}

	@Test
	void whenUserWithUsernameAlreadyExistsThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.of(UserEntityGenerator.generateOther()));
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_USERNAME_ALREADY_EXISTS));

		verify(userRepository).findByUsername(userSimple.getUsername());
		verify(userRepository, never()).findByEmail(userSimple.getEmail());
		verify(userRepository, never()).findById(user.getId());
		verify(userRepository, never()).save(changedUserEntity);
	}

	@Test
	void whenUserWithEmailAlreadyExistsThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();
		UserEntity userEntity = UserEntityGenerator.generate();
		UserEntity changedUserEntity = UserEntityGenerator.generateChanged();
		UserSimple userSimple = UserSimpleGenerator.generateChanged();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(userEntity));
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.of(UserEntityGenerator.generateOther()));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(user))
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_EMAIL_ALREADY_EXISTS));

		verify(userRepository).findByUsername(userSimple.getUsername());
		verify(userRepository).findByEmail(userSimple.getEmail());
		verify(userRepository, never()).findById(user.getId());
		verify(userRepository, never()).save(changedUserEntity);
	}

	@Test
	void whenAnonymousThenRedirect() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));

		verify(userRepository, never()).findById(anyLong());
	}

	@Test
	void whenAnonymousWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findById(anyLong());
	}

	@Test
	void whenUserWithoutCsrfThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());

		verify(userRepository, never()).findById(anyLong());
	}


}
