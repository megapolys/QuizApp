package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.repositories.QuizDecisionRepository;
import com.example.servingwebcontent.service.QuizDecisionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/decisions")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizDecisionController {

    private final QuizDecisionRepository quizDecisionRepository;
    private final QuizDecisionService quizDecisionService;

    public QuizDecisionController(QuizDecisionRepository quizDecisionRepository, QuizDecisionService quizDecisionService) {
        this.quizDecisionRepository = quizDecisionRepository;
        this.quizDecisionService = quizDecisionService;
    }

    @GetMapping
    public String getDecisions(Model model) {
        model.addAttribute("decisions", quizDecisionRepository.findAll());
        return "decisions";
    }

    @GetMapping("/add")
    public String addDecision(
        @RequestParam String name,
        RedirectAttributes redirectAttributes
    ) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Пустое имя!");
        } else {
            final QuizDecision decision = new QuizDecision();
            decision.setName(name);
            final QuizDecisionService.DecisionResult result = quizDecisionService.add(decision);
            if (result.result() == QuizDecisionService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Такое имя уже занято.");
            }
        }
        return "redirect:/decisions";
    }

    @GetMapping("/delete/{decision}")
    public String deleteDecision(
            @PathVariable QuizDecision decision
    ) {
        quizDecisionService.delete(decision);
        return "redirect:/decisions";
    }

}
