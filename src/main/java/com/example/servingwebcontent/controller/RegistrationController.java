package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.dto.CaptchaResponseDto;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Controller
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Value("${captcha.secret}")
    private String captchaSecret;

    private final UserService userService;
    private final RestTemplate restTemplate;

    public RegistrationController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            User user,
            Model model
    ) {
        final String url = String.format(CAPTCHA_URL, captchaSecret, captchaResponse);
        final CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
        model.addAttribute("user", user);
        if (response != null && !response.isSuccess()) {
            model.addAttribute("message", "Ошибка капчи");
            return "registration";
        }
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPassword2())
                || StringUtils.isBlank(user.getEmail()) || user.getYearBorn() == null || StringUtils.isBlank(user.getLastName())
                || StringUtils.isBlank(user.getFirstName())) {
            model.addAttribute("message", "Необходимо заполнить все поля");
            return "registration";
        }
        if (user.getMale() == null) {
            model.addAttribute("message", "Вам необходимо определиться с гендером");
            return "registration";
        }
        if (!Objects.equals(user.getPassword(), user.getPassword2())) {
            model.addAttribute("message", "Пароли не совпадают");
            return "registration";
        }
        UserService.UserResult result = userService.addUser(user);
        if (result.result() == UserService.ResultType.USERNAME_FOUND) {
            model.addAttribute("message", "Пользователь с таким логином уже существует!");
        }
        if (result.result() == UserService.ResultType.EMAIL_FOUND) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует!");
        }
        if (result.result() == UserService.ResultType.SUCCESS) {
            return "redirect:/activate/activation";
        } else {
            return "registration";
        }
    }

    @GetMapping("/login{code}")
    public String login(
            @RequestParam Map<String, String> params,
            Model model
    ) {
        String message = null;
        if (params.containsKey("error")) {
            message = "Ошибочка, похоже логин или пароль не подходят";
            model.addAttribute("repairPassword", true);
        } else if (params.containsKey("logout")) {
            message = "Вы покинули нас...";
        }
        model.addAttribute("message", message);
        return "login";
    }


    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        final boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("successMessage", "Пользователь успешно активирован");
        } else {
            model.addAttribute("message", "Код активации не найден");
        }

        return "login";
    }

    @GetMapping("/activate/activation")
    public String activate() {
        return "activation";
    }

    @GetMapping("/repairPassword")
    public String repairPassword(Model model) {
        return "repairPassword";
    }

    @PostMapping("/repairPassword")
    public String repairPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        if (StringUtils.isBlank(email)) {
            redirectAttributes.addFlashAttribute("message", "Введите свою почту");
            return "redirect:/repairPassword";
        }
        final UserService.UserResult result = userService.repairPassword(email);
        if (result.result() == UserService.ResultType.EMAIL_FOUND) {
            redirectAttributes.addFlashAttribute("successMessage", "Новый пароль выслан на вашу электронную почту.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Пользователя с данной почтой не обнаружено.");
        }
        return "redirect:/repairPassword";
    }

    @GetMapping("/repairPassword/{repairPasswordCode}")
    public String repairPasswordByCode(
            @PathVariable String repairPasswordCode,
            Model model
    ) {
        final UserService.UserResult result = userService.findByRepairPasswordCode(repairPasswordCode);
        if (result.result() == UserService.ResultType.SUCCESS) {
            model.addAttribute("validCode", repairPasswordCode);
            return "repairPassword";
        } else {
            model.addAttribute("message", "Невалидная ссылка на восстановление пароля");
        }
        return "login";
    }

    @PostMapping("/repairPassword/{repairPasswordCode}")
    public String repairPasswordByCode(
            @PathVariable String repairPasswordCode,
            User user,
            RedirectAttributes redirectAttributes
    ) {
        final UserService.UserResult result = userService.findByRepairPasswordCode(repairPasswordCode);
        if (result.result() == UserService.ResultType.SUCCESS) {
            if (StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getPassword2())) {
                redirectAttributes.addFlashAttribute("message", "Необходимо заполнить все поля");
                redirectAttributes.addAttribute("code", repairPasswordCode);
                return "redirect:/repairPassword/{code}";
            } else if (!Objects.equals(user.getPassword(), user.getPassword2())) {
                redirectAttributes.addFlashAttribute("message", "Пароли должны совпадать");
                redirectAttributes.addAttribute("code", repairPasswordCode);
                return "redirect:/repairPassword/{code}";
            } else {
                userService.updatePassword(result.user(), user);
                redirectAttributes.addFlashAttribute("successMessage", "Пароль успешно обновлен.");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Невалидная ссылка на восстановление пароля");
        }
        return "redirect:/login";
    }
}
