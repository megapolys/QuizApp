package com.example.servingwebcontent.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DecisionController {

    /**
     * Получение всех решений вместе с группами
     *
     * @return decisionTab - Активная вкладка решений
     */
    @GetMapping("/decisions")
    public String getDecisions(Model model) {
        model.addAttribute("decisionTab", "active");
        return "decisions/decisions";
    }
}
