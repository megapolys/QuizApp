package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.UserRepository;
import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String editUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String updateUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam(name="roles", required=false) String[] roles,
            @RequestParam("userId") User user
    ) {
        user.setUsername(username);
        user.setEmail(email);
        final List<String> staticRoles = Arrays.stream(Role.values()).map(Role::name).toList();
        if (roles != null) {
            user.setRoles(Arrays.stream(roles).filter(staticRoles::contains).map(Role::valueOf).collect(Collectors.toSet()));
        }
        userRepository.save(user);
        return "redirect:/user";
    }
}
