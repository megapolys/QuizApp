package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.quiz.decision.DecisionGroup;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import com.example.servingwebcontent.service.DecisionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/decisions")
@PreAuthorize("hasAuthority('ADMIN')")
public class DecisionController {

    private final DecisionService decisionService;

    public DecisionController(DecisionService decisionService) {
        this.decisionService = decisionService;
    }

    @GetMapping
    public String getDecisions(Model model) {
        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
        model.addAttribute("groups", decisionService.groups());
        model.addAttribute("decisionTab", "active");
        return "decisions/decisions";
    }

    @PostMapping("/group/add")
    public String addGroup(
            @RequestParam String name,
            RedirectAttributes redirectAttributes
    ) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Пустое название группы!");
        } else {
            final DecisionGroup group = new DecisionGroup();
            group.setName(name.trim());
            final DecisionService.ResultType result = decisionService.add(group);
            if (result == DecisionService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Такое имя группы уже занято.");
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Группа добавлена");
        return "redirect:/decisions";
    }

    @PostMapping("/group/update/{group}")
    public String groupUpdate(
            @PathVariable DecisionGroup group,
            @RequestParam(required = false) String name,
            RedirectAttributes redirectAttributes
    ) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Пустое название группы!");
            redirectAttributes.addFlashAttribute("changeGroup", group);
        } else {
            final DecisionGroup newGroup = new DecisionGroup();
            newGroup.setId(group.getId());
            newGroup.setDecisions(group.getDecisions());
            newGroup.setName(name.trim());
            final DecisionService.ResultType result = decisionService.update(newGroup);
            if (result == DecisionService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Такое имя группы уже занято.");
                redirectAttributes.addFlashAttribute("changeGroup", group);
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Группа обновлена");
        return "redirect:/decisions";
    }

    @GetMapping("/group/updateAction/{group}")
    public String groupUpdateAction(
            @PathVariable DecisionGroup group,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("changeGroup", group);
        return "redirect:/decisions";
    }

    @PostMapping("/group/delete/{group}")
    public String groupDelete(
            @PathVariable DecisionGroup group,
            RedirectAttributes redirectAttributes
    ) {
        decisionService.delete(group);
        redirectAttributes.addFlashAttribute("successMessage", "Группа удалена");
        return "redirect:/decisions";
    }

    @PostMapping("/add")
    public String addDecision(
            @RequestParam(required = false) DecisionGroup group,
            @RequestParam String name,
            @RequestParam String description,
            RedirectAttributes redirectAttributes
    ) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Пустое имя!");
        } else {
            final QuizDecision decision = new QuizDecision();
            decision.setName(name.trim());
            decision.setDescription(description != null ? description.trim() : null);
            decision.setGroup(group);
            final DecisionService.ResultType result = decisionService.add(decision);
            if (result == DecisionService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Такое имя решения уже занято.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Решение добавлено");
            }
        }
        return "redirect:/decisions";
    }

    @PostMapping("/update/{decision}")
    public String decisionUpdate(
            @PathVariable QuizDecision decision,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam(required = false) DecisionGroup group,
            RedirectAttributes redirectAttributes
    ) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Пустое имя!");
            redirectAttributes.addFlashAttribute("changeDecision", decision);
        } else {
            final QuizDecision newDecision = new QuizDecision();
            newDecision.setId(decision.getId());
            newDecision.setName(name.trim());
            newDecision.setDescription(description != null ? description.trim() : null);
            newDecision.setGroup(group);
            final DecisionService.ResultType result = decisionService.update(newDecision);
            if (result == DecisionService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Такое имя решения уже занято.");
                redirectAttributes.addFlashAttribute("changeDecision", decision);
                return "redirect:/decisions";
            }
        }
        redirectAttributes.addFlashAttribute("successMessage", "Решение обновлено");
        return "redirect:/decisions";
    }

    @GetMapping("/updateAction/{decision}")
    public String decisionUpdateAction(
            @PathVariable QuizDecision decision,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("changeDecision", decision);
        return "redirect:/decisions";
    }

    @PostMapping("/delete/{decision}")
    public String deleteDecision(
            @PathVariable QuizDecision decision,
            RedirectAttributes redirectAttributes
    ) {
        decisionService.delete(decision);
        redirectAttributes.addFlashAttribute("successMessage", "Решение удалено");
        return "redirect:/decisions";
    }

}
