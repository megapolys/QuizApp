package com.example.servingwebcontent;

import com.example.accessingdatadb.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
//    @Autowired
//    private UserRepository userRepository;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }


//    @PostMapping(path="/add")
//    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
//        final User user = new User();
//        user.setName(name);
//        user.setEmail(email);
//        userRepository.save(user);
//        return "Saved";
//    }
//
//    @GetMapping(path="/all")
//    public @ResponseBody Iterable<User> getAllUsers() {
//        return userRepository.findAll();
//    }
}
