package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.service.decision.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class DecisionController {

    private final DecisionService decisionService;

    /**
     * Получение всех решений вместе с группами
     *
     * @return decisions - Решения без групп
     * groups - Группы с решениями
     * decisionTab - Активная вкладка решений
     */
    @GetMapping("/decisions")
    public String getDecisions(Model model) {
        model.addAttribute("decisionTab", "active");
        return "decisions/decisions";
    }

    /**
     * Получение страницы обновления решения
     *
     * @param decisionId - Идентификатор решения
     */
    @GetMapping("/decisions/updateAction/{decisionId}")
    public String decisionUpdateAction(
        @PathVariable Long decisionId,
        RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("changeDecision", decisionService.getDecisionById(decisionId));
        return "redirect:/decisions";
    }
}
