package com.example.servingwebcontent.controller.registration;

import com.example.servingwebcontent.generator.CaptchaResponseDtoGenerator;
import com.example.servingwebcontent.generator.role.RoleEntityGenerator;
import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.generator.user.UserSimpleWithPasswordGenerator;
import com.example.servingwebcontent.model.dto.CaptchaResponseDto;
import com.example.servingwebcontent.model.entities.RoleEntity;
import com.example.servingwebcontent.model.entities.UserEntity;
import com.example.servingwebcontent.model.entities.UserRoleEntity;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.List;
import java.util.Optional;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.USER_ROLE_NAME;
import static com.example.servingwebcontent.consts.MessageConsts.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthController_Register_Test extends AuthControllerTest {

	private final static String URL = "/registration";

	private final static String CAPTCHA_PARAM = "g-recaptcha-response";
	private final static String USERNAME_PARAM = "username";
	private final static String LAST_NAME_PARAM = "lastName";
	private final static String FIRST_NAME_PARAM = "firstName";
	private final static String EMAIL_PARAM = "email";
	private final static String BIRTHDAY_PARAM = "birthday";
	private final static String GENDER_PARAM = "male";
	private final static String PASSWORD_PARAM = "password";
	private final static String PASSWORD2_PARAM = "password2";

	@Test
	void whenAnonymousTryRegistrationThenSuccessRedirect() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		UserRoleEntity userRoleEntity = UserRoleEntity.createNew(roleEntity.getId(), roleEntity.getId());
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));
		when(passwordEncoder.encode(userSimple.getPassword())).thenReturn(userEntity.getPassword());

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2())
			)
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/activate"));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository).findByUsername(userSimple.getUsername());
		verify(userRepository).findByEmail(userSimple.getEmail());
		verify(roleRepository).findByName(USER_ROLE_NAME);
		verify(userRoleRepository).save(eq(userRoleEntity));
		verify(passwordEncoder).encode(anyString());

		ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
		verify(userRepository).save(userEntityArgumentCaptor.capture());
		UserEntity actualUserEntity = userEntityArgumentCaptor.getValue();
		then(actualUserEntity).isNotNull();
		then(actualUserEntity.getId()).isNull();
		then(actualUserEntity.getUsername()).isEqualTo(userEntity.getUsername());
		then(actualUserEntity.getPassword()).isEqualTo(userEntity.getPassword());
		then(actualUserEntity.getLastName()).isEqualTo(userEntity.getLastName());
		then(actualUserEntity.getFirstName()).isEqualTo(userEntity.getFirstName());
		then(actualUserEntity.getBirthday()).isEqualTo(userEntity.getBirthday());
		then(actualUserEntity.getEmail()).isEqualTo(userEntity.getEmail());
		then(actualUserEntity.isActive()).isFalse();
		then(actualUserEntity.isMale()).isEqualTo(userEntity.isMale());
		then(actualUserEntity.getActivationCode()).isNotBlank();
		then(actualUserEntity.getRepairPasswordCode()).isNull();

		ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
		verify(mailSender).send(simpleMailMessageArgumentCaptor.capture());
		SimpleMailMessage simpleMailMessage = simpleMailMessageArgumentCaptor.getValue();
		then(simpleMailMessage).isNotNull();
		then(simpleMailMessage.getTo()).containsOnly(userSimple.getEmail());
		then(simpleMailMessage.getFrom()).isNotNull();
		then(simpleMailMessage.getSubject()).isNotNull();
		then(simpleMailMessage.getText()).contains(actualUserEntity.getActivationCode());
	}

	@Test
	void whenWithBadRecaptchaThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateFailed());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, CAPTCHA_EXCEPTION_MESSAGE));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository, never()).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenWithEmptyUsernameThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository, never()).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenWithEmptyAllUserFieldsThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository, never()).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenDifferentPasswordsThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2() + "_other")
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, DIFFERENT_PASSWORDS));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository, never()).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenUserWithUsernameAlreadyExistsThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.of(UserEntityGenerator.generateOther()));
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.empty());
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_USERNAME_ALREADY_EXISTS));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository).findByUsername(anyString());
		verify(userRepository, never()).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenUserWithEmailAlreadyExistsThenSuccess() throws Exception {
		UserEntity userEntity = UserEntityGenerator.generate();
		UserSimpleWithPassword userSimple = UserSimpleWithPasswordGenerator.generate();
		RoleEntity roleEntity = RoleEntityGenerator.generate();
		String captcha = "captcha";

		when(restTemplate.postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class))).thenReturn(CaptchaResponseDtoGenerator.generateSuccess());
		when(userRepository.findByUsername(userSimple.getUsername())).thenReturn(Optional.empty());
		when(userRepository.findByEmail(userSimple.getEmail())).thenReturn(Optional.of(UserEntityGenerator.generateOther()));
		when(userRepository.save(any())).thenReturn(userEntity);
		when(roleRepository.findByName(USER_ROLE_NAME)).thenReturn(Optional.of(roleEntity));

		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.anonymous())
				.param(CAPTCHA_PARAM, captcha)
				.param(USERNAME_PARAM, userSimple.getUsername())
				.param(LAST_NAME_PARAM, userSimple.getLastName())
				.param(FIRST_NAME_PARAM, userSimple.getFirstName())
				.param(EMAIL_PARAM, userSimple.getEmail())
				.param(BIRTHDAY_PARAM, userSimple.getBirthday().toString())
				.param(GENDER_PARAM, userSimple.getMale().toString())
				.param(PASSWORD_PARAM, userSimple.getPassword())
				.param(PASSWORD2_PARAM, userSimple.getPassword2())
			)
			.andExpect(status().isOk())
			.andExpect(view().name("auth/registration"))
			.andExpect(model().attribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_EMAIL_ALREADY_EXISTS));

		verify(restTemplate).postForObject(anyString(), eq(List.of()), eq(CaptchaResponseDto.class));
		verify(userRepository).findByUsername(anyString());
		verify(userRepository).findByEmail(anyString());
		verify(roleRepository, never()).findByName(anyString());
		verify(userRoleRepository, never()).save(any());
		verify(userRepository, never()).save(any());
		verify(mailSender, never()).send(any(SimpleMailMessage.class));
	}

	@Test
	void whenAnonymousWithoutCsrfTryRegistrationThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().isForbidden());
	}

	@Test
	void whenUserTryRegistrationThenForbidden() throws Exception {
		mockMvc.perform(post(URL)
				.with(csrf())
				.with(SecurityMockMvcRequestPostProcessors.user(UserDetailsCustomGenerator.generate())))
			.andExpect(status().isForbidden());
	}

}
