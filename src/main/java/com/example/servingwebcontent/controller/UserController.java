package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.exceptions.UserAlreadyExistsByEmailException;
import com.example.servingwebcontent.exceptions.UserAlreadyExistsByUsernameException;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.*;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	/**
	 * Получение списка пользователей
	 * Доступно только администратору
	 *
	 * @return users    - Список пользователей
	 * usersTab - Активная вкладка
	 */
	@GetMapping("/user/list")
	public String userList(Model model) {
		model.addAttribute("users", userService.findAll());
		model.addAttribute("usersTab", "active");
		return "user/list";
	}

	/**
	 * Получение своего профиля
	 *
	 * @param user - Автор запроса
	 *
	 * @return user - Профиль пользователя
	 */
	@GetMapping("/user")
	public String getProfile(
		@AuthenticationPrincipal UserDetailsCustom user,
		Model model
	) {
		if (user == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", userService.getSimpleUserById(user.getId()));
		return "user/profile";
	}

	/**
	 * Получение формы изменения своего профиля
	 *
	 * @param user - Автор запроса
	 *
	 * @return user - Профиль пользователя
	 */
	@GetMapping("/user/edit")
	public String editProfile(
		@AuthenticationPrincipal UserDetailsCustom user,
		Model model
	) {
		model.addAttribute("user", userService.getSimpleUserById(user.getId()));
		return "user/editProfile";
	}

	/**
	 * Запрос на изменение своего профиля
	 *
	 * @param currentUser - Автор запроса
	 * @param user        - Новые данные профиля
	 *
	 * @return user           - Профиль пользователя (при неудачном изменении)
	 * message        - Сообщение об ошибке
	 * successMessage - Сообщение об удачном изменении
	 */
	@PostMapping("/user/edit")
	public String editProfile(
		@AuthenticationPrincipal UserDetailsCustom currentUser,
		UserSimple user,
		Model model,
		RedirectAttributes redirectAttributes
	) {
		model.addAttribute("user", user);
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getEmail()) || user.getBirthday() == null
			|| StringUtils.isBlank(user.getLastName()) || StringUtils.isBlank(user.getFirstName())) {
			model.addAttribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS);
			return "user/editProfile";
		}
		try {
			userService.updateUser(currentUser.getId(), user);
		} catch (UserAlreadyExistsByUsernameException ignore) {
			model.addAttribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_USERNAME_ALREADY_EXISTS);
			return "user/editProfile";
		} catch (UserAlreadyExistsByEmailException ignore) {
			model.addAttribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_EMAIL_ALREADY_EXISTS);
			return "user/editProfile";
		}
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, CHANGES_COMPLETE_SUCCESSFUL);
		return "redirect:/user";
	}
}
