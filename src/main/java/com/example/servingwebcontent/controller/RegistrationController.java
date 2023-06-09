package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
            model.addAttribute("message", "Необходимо заполнить логин и пароль");
            return "registration";
        }
        if (userService.addUser(user) == null) {
            model.addAttribute("message", "Пользователь с таким именем уже существует!");
            return "registration";
        }
        return "redirect:/login";
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

    @GetMapping("/testMail")
    public String testMail() {
        userService.testMail();
        return "redirect:/registration";
    }
}
