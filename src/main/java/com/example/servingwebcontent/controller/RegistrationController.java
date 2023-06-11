package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        model.addAttribute("user", user);
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
        || StringUtils.isBlank(user.getEmail())) {
            model.addAttribute("message", "Необходимо заполнить все поля");
            return "redirect:/registration";
        }
        UserService.UserResult result = userService.addUser(user);
        if (result.result() == UserService.ResultType.USERNAME_FOUND) {
            model.addAttribute("message", "Пользователь с таким логином уже существует!");
            return "redirect:/registration";
        }
        if (result.result() == UserService.ResultType.EMAIL_FOUND) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует!");
            return "redirect:/registration";
        }
        return "redirect:/activate/activation";
    }

    @GetMapping("/login{code}")
    public String login(
            @RequestParam Map<String, String> params,
            Model model
    ) {

        String message = null;
        if (params.containsKey("error")) {
            message = "Ошибочка, похоже логин или пароль не подходит...";
        } else if(params.containsKey("logout")) {
            message = "Вы покинули нас...";
        }
        model.addAttribute("message", message);
        return "login";
    }


    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        final boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "Пользователь успешно активирован");
        } else {
            model.addAttribute("message", "Код активации не найден");
        }

        return "login";
    }

    @GetMapping("/activate/activation")
    public String activate() {
        return "activation";
    }
}
