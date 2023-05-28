package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.UserRepository;
import com.example.servingwebcontent.domain.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }


    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("findByName")
    public String findByName(@RequestParam String find_name, Model model) {
        if (StringUtils.isNotBlank(find_name)) {
            model.addAttribute("users", userRepository.findByUsernameContaining(find_name));
        } else {
            model.addAttribute("users", userRepository.findAll());
        }
        return "main";
    }
}
