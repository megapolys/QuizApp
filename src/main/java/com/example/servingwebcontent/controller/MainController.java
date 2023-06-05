package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.domain.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/main")
    public String main(@RequestParam(required = false) String findName, @AuthenticationPrincipal User user, Model model) {
        if (StringUtils.isNotBlank(findName)) {
            model.addAttribute("users", userRepository.findByUsernameContaining(findName));
        } else {
            model.addAttribute("users", userRepository.findAll());
        }
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("findNameFilter", findName);
        return "main";
    }
}
