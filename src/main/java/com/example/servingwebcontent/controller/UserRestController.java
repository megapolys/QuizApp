package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRestController {

	private final UserService userService;

	/**
	 * Получение списка пользователей
	 *
	 * @return Список пользователей
	 */
	@GetMapping("api/user/all")
	public List<UserSimple> getUserList() {
		return userService.findAll();
	}

	/**
	 * Получение списка пользователей
	 *
	 * @return Список пользователей
	 */
	@GetMapping("api/user/{id}")
	public UserSimple getUserById(@PathVariable Long id) {
		return userService.getSimpleUserById(id);
	}
}
