package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.repositories.QuizRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.domain.Role;
import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, QuizRepository quizRepository, UserService userService) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.userService = userService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user/userList";
    }

    @GetMapping("/{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUser(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Arrays.asList(Role.values()));
        model.addAttribute("quizzes", quizRepository.findAll());
        return "user/userEdit";
    }

    @GetMapping
    public String getProfile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", userRepository.findById(user.getId()).orElse(null));
        return "user/profile";
    }

    @GetMapping("/edit")
    public String editProfile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("user", userRepository.findById(user.getId()).orElse(null));
        return "user/editProfile";
    }

    @PostMapping("/edit")
    public String editProfile(
            @AuthenticationPrincipal User currentUser,
            User user,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        currentUser = userRepository.findById(currentUser.getId()).orElse(null);
        model.addAttribute("user", user);
        if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getEmail()) || user.getYearBorn() == null
                || StringUtils.isBlank(user.getLastName()) || StringUtils.isBlank(user.getFirstName())) {
            model.addAttribute("message", "Необходимо заполнить все поля");
            return "user/editProfile";
        }
        if (!Objects.equals(user.getPassword(), user.getPassword2())) {
            model.addAttribute("message", "Пароли не совпадают");
            return "user/editProfile";
        }
        UserService.UserResult result = userService.updateUser(currentUser, user);
        if (result.result() == UserService.ResultType.USERNAME_FOUND) {
            model.addAttribute("message", "Пользователь с таким логином уже существует!");
            return "user/editProfile";
        }
        if (result.result() == UserService.ResultType.EMAIL_FOUND) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует!");
            return "user/editProfile";
        }
        redirectAttributes.addFlashAttribute("successMessage", "Изменения успешно сохранены");
        return "redirect:/user";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(
            @RequestParam(name = "roles", required = false) String[] roles,
            @RequestParam(name = "quizzes", required = false) Quiz[] quizzes,
            @RequestParam("userId") User user
    ) {
        final List<String> staticRoles = Arrays.stream(Role.values()).map(Role::name).toList();
        if (roles != null) {
            user.setRoles(Arrays.stream(roles).filter(staticRoles::contains).map(Role::valueOf).collect(Collectors.toSet()));
        }
        if (quizzes != null) {
            user.setQuizzes(Arrays.stream(quizzes).collect(Collectors.toSet()));
        } else {
            user.setQuizzes(new LinkedHashSet<>());
        }
        userRepository.save(user);
        return "redirect:/user/list";
    }
}
