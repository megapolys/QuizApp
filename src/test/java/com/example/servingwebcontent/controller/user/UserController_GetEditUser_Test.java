package com.example.servingwebcontent.controller.user;

import com.example.servingwebcontent.generator.user.UserDetailsCustomGenerator;
import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.generator.user.UserSimpleGenerator;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserController_GetEditUser_Test extends UserControllerTest {

	private final static String URL = "/user/edit";

	@Test
	void whenUserNotNullThenSuccess() throws Exception {
		UserDetailsCustom user = UserDetailsCustomGenerator.generate();

		when(userRepository.findById(user.getId())).thenReturn(Optional.of(UserEntityGenerator.generate()));

		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.user(user)))
			.andExpect(status().isOk())
			.andExpect(view().name("user/editProfile"))
			.andExpect(model().attribute("user", UserSimpleGenerator.generate()));

		verify(userRepository).findById(user.getId());
	}

	@Test
	void whenAnonymousThenRedirect() throws Exception {
		mockMvc.perform(get(URL)
				.with(SecurityMockMvcRequestPostProcessors.anonymous()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));

		verify(userRepository, never()).findById(anyLong());
	}

}
