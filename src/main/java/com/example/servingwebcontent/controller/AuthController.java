package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.exceptions.UserAlreadyExistsByEmailException;
import com.example.servingwebcontent.exceptions.UserAlreadyExistsByUsernameException;
import com.example.servingwebcontent.exceptions.UserNotFoundException;
import com.example.servingwebcontent.model.dto.CaptchaResponseDto;
import com.example.servingwebcontent.model.user.Password;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

import static com.example.servingwebcontent.consts.Consts.*;
import static com.example.servingwebcontent.consts.MessageConsts.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
	@Value("${captcha.secret}")
	private String captchaSecret;

	private final UserService userService;
	private final RestTemplate restTemplate;

	/**
	 * Получение страницы регистрации
	 * Доступно только анонимосу
	 */
	@GetMapping("/registration")
	public String registration() {
		return "auth/registration";
	}

	/**
	 * Получение списка пользователей
	 * Доступно только анонимосу
	 *
	 * @param captchaResponse - Рекапча
	 * @param user            - Данные профиля
	 *
	 * @return message - Сообщение об ошибке
	 */
	@PostMapping("/registration")
	public String register(
		@RequestParam("g-recaptcha-response") String captchaResponse,
		UserSimpleWithPassword user,
		Model model
	) {
		final String url = String.format(CAPTCHA_URL, captchaSecret, captchaResponse);
		final CaptchaResponseDto response = restTemplate.postForObject(url, List.of(), CaptchaResponseDto.class);
		model.addAttribute("user", user);
		if (response != null && !response.isSuccess()) {
			model.addAttribute(ERROR_MESSAGE_PARAM, CAPTCHA_EXCEPTION_MESSAGE);
			return "auth/registration";
		}
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPassword2())
			|| StringUtils.isBlank(user.getEmail()) || user.getBirthday() == null || StringUtils.isBlank(user.getLastName())
			|| StringUtils.isBlank(user.getFirstName())) {
			model.addAttribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS);
			return "auth/registration";
		}
		if (!Objects.equals(user.getPassword(), user.getPassword2())) {
			model.addAttribute(ERROR_MESSAGE_PARAM, DIFFERENT_PASSWORDS);
			return "auth/registration";
		}
		try {
			userService.register(user);
		} catch (UserAlreadyExistsByUsernameException ignore) {
			model.addAttribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_USERNAME_ALREADY_EXISTS);
			return "auth/registration";
		} catch (UserAlreadyExistsByEmailException ignore) {
			model.addAttribute(ERROR_MESSAGE_PARAM, PROFILE_WITH_SAME_EMAIL_ALREADY_EXISTS);
			return "auth/registration";
		}
		return "redirect:/activate";
	}

	/**
	 * Получение страницы аутентификации по логину и паролю
	 */
	@GetMapping(value = "/login")
	public String login() {
		return "auth/login";
	}

	/**
	 * Получение страницы аутентификации по логину и паролю при ошибке
	 *
	 * @return message - Сообщение об ошибке
	 * repairPassword - Отображать информацию об отображении ссылки на восстановление пароля
	 */
	@GetMapping(value = "/login", params = "error")
	public String loginWithError(
		Model model
	) {
		model.addAttribute(ERROR_MESSAGE_PARAM, LOGIN_EXCEPTION);
		model.addAttribute("repairPassword", true);
		return "auth/login";
	}

	/**
	 * Получение страницы аутентификации по логину и паролю при выходе
	 *
	 * @return message - Сообщение об ошибке
	 */
	@GetMapping(value = "/login", params = "logout")
	public String loginWithLogout(
		Model model
	) {
		model.addAttribute(ERROR_MESSAGE_PARAM, LOGIN_LOGOUT_EXCEPTION);
		return "auth/login";
	}

	/**
	 * Получение страницы с информацией о высланной на почту ссылке активации профиля
	 */
	@GetMapping("/activate")
	public String getActivate() {
		return "auth/activation";
	}

	/**
	 * Активация профиля по ссылке
	 *
	 * @param code - Код активации
	 */
	@GetMapping("/activate/{code}")
	public String getActivateByCode(
		Model model,
		@PathVariable String code
	) {
		if (userService.activateUser(code)) {
			model.addAttribute(SUCCESS_MESSAGE_PARAM, PROFILE_SUCCESSFUL_ACTIVATED);
		} else {
			model.addAttribute(ERROR_MESSAGE_PARAM, ACTIVATION_CODE_NOT_FOUND);
		}
		return "auth/login";
	}

	/**
	 * Получение страницы восстановления пароля
	 */
	@GetMapping("/repairPassword")
	public String getRepairPassword() {
		return "auth/repairPassword";
	}

	/**
	 * Получение ссылки на восстановление пароля на почту
	 *
	 * @param email - Почта
	 */
	@PostMapping("/repairPassword")
	public String repairPassword(
		@RequestParam String email,
		Model model
	) {
		if (StringUtils.isBlank(email)) {
			model.addAttribute(ERROR_MESSAGE_PARAM, EMAIL_REQUIRED);
			return "auth/repairPassword";
		}
		if (userService.repairPassword(email)) {
			model.addAttribute(SUCCESS_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_SENT);
		} else {
			model.addAttribute(ERROR_MESSAGE_PARAM, PROFILE_BY_EMAIL_NOT_FOUND);
		}
		return "auth/repairPassword";
	}

	/**
	 * Получение формы изменения пароля по коду восстановления пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 */
	@GetMapping("/repairPassword/{repairPasswordCode}")
	public String repairPasswordByCode(
		@PathVariable String repairPasswordCode,
		Model model
	) {
		if (userService.existsByRepairPasswordCode(repairPasswordCode)) {
			model.addAttribute("validCode", repairPasswordCode);
			return "auth/repairPassword";
		} else {
			model.addAttribute(ERROR_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_NOT_FOUND);
			return "auth/login";
		}
	}

	/**
	 * Изменение пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 * @param password           - Новый пароль
	 */
	@PostMapping("/repairPassword/{repairPasswordCode}")
	public String repairPasswordByCode(
		@PathVariable String repairPasswordCode,
		Password password,
		RedirectAttributes redirectAttributes
	) {
		try {
			if (StringUtils.isBlank(password.getPassword()) || StringUtils.isBlank(password.getPassword2())) {
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, REQUIRE_ALL_FIELDS);
				redirectAttributes.addAttribute("code", repairPasswordCode);
				return "redirect:/repairPassword/{code}";
			} else if (!Objects.equals(password.getPassword(), password.getPassword2())) {
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, DIFFERENT_PASSWORDS);
				redirectAttributes.addAttribute("code", repairPasswordCode);
				return "redirect:/repairPassword/{code}";
			} else {
				userService.updatePassword(repairPasswordCode, password.getPassword());
				redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, PASSWORD_SUCCESSFUL_CHANGED);
				return "redirect:/login";
			}
		} catch (UserNotFoundException ignore) {
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, REPAIR_PASSWORD_CODE_NOT_FOUND);
			return "redirect:/login";
		}
	}
}
