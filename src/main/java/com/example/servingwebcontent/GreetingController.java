package com.example.servingwebcontent;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }


    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("/main")
    public String addNewUser(@RequestParam String name, @RequestParam String email, Model model) {
        final User user = new User();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }

    @PostMapping("findByName")
    public String findByName(@RequestParam String find_name, Model model) {
        if (StringUtils.isNotBlank(find_name)) {
            model.addAttribute("users", userRepository.findByNameContaining(find_name));
        } else {
            model.addAttribute("users", userRepository.findAll());
        }
        return "main";
    }
}
