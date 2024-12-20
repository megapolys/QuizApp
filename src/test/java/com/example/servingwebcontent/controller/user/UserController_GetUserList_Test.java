package com.example.servingwebcontent.controller.user;

import com.example.servingwebcontent.generator.user.UserEntityGenerator;
import com.example.servingwebcontent.generator.user.UserSimpleGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserController_GetUserList_Test extends UserControllerTest {

	private final static String URL = "/user/list";

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenFoundUsersThenSuccess() throws Exception {
		when(userRepository.findAll()).thenReturn(UserEntityGenerator.generateList());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("user/list"))
			.andExpect(model().attribute("users", UserSimpleGenerator.generateList()))
			.andExpect(model().attribute("usersTab", "active"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenOneUserThenSuccess() throws Exception {
		when(userRepository.findAll()).thenReturn(UserEntityGenerator.generateOneList());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("user/list"))
			.andExpect(model().attribute("users", UserSimpleGenerator.generateOneList()))
			.andExpect(model().attribute("usersTab", "active"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	void whenEmptyUsersThenSuccess() throws Exception {
		when(userRepository.findAll()).thenReturn(List.of());

		mockMvc.perform(get(URL))
			.andExpect(status().isOk())
			.andExpect(view().name("user/list"))
			.andExpect(model().attribute("users", List.of()))
			.andExpect(model().attribute("usersTab", "active"));
	}

	@Test
	@WithMockUser
	void whenRequestNotAdminThenForbidden() throws Exception {
		when(userRepository.findAll()).thenReturn(UserEntityGenerator.generateList());

		mockMvc.perform(get(URL))
			.andExpect(status().isForbidden());
	}

}
