package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.UserRepository;
import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "Username exists!");
            return "registration";
        } else if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
                || StringUtils.isBlank(user.getEmail())) {
              model.addAttribute("message", "Необходимо заполнить все поля");
              return "registration";
        } else {
            user.setActive(true);
            user.setRoles(Set.of(Role.USER));
            userRepository.save(user);
        }
        return "redirect:/login";
    }
}
