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

    @GetMapping("/main")
    public String getMain() {
        return "main";
    }


}
