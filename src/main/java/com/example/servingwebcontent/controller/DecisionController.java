package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.exceptions.DecisionAlreadyExistsByNameException;
import com.example.servingwebcontent.exceptions.GroupAlreadyExistsByNameException;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.service.decision.DecisionService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.example.servingwebcontent.consts.Consts.ERROR_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.*;

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
        model.addAttribute("decisions", decisionService.getUngroupedDecisions());
        model.addAttribute("groups", decisionService.getDecisionGroups());
        model.addAttribute("decisionTab", "active");
        return "decisions/decisions";
    }

    /**
     * Добавление новой группы
     */
    @PostMapping("/decisions/group")
    public String addGroup(
        Group group,
        RedirectAttributes redirectAttributes
    ) {
        if (StringUtils.isBlank(group.getName())) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, GROUP_NAME_IS_EMPTY);
        } else {
            try {
                decisionService.addGroup(group);
            } catch (GroupAlreadyExistsByNameException e) {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
            }
        }
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_ADDED);
        return "redirect:/decisions";
    }

    /**
     * Получение формы обновления группы
     *
     * @param groupId - Идентификатор группы
     *
     * @return changeGroup - Группа
     */
    @GetMapping("/decisions/group/updateAction/{groupId}")
    public String getGroupUpdate(
        @PathVariable Long groupId,
        RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("changeGroup", decisionService.getGroupById(groupId));
        return "redirect:/decisions";
    }

    /**
     * Изменение группы
     *
     * @param groupId - Идентификатор группы
     *
     * @return changeGroup - Группа
     */
    @PostMapping("/decisions/group/{groupId}")
    public String groupUpdate(
        @PathVariable Long groupId,
        Group group,
        RedirectAttributes redirectAttributes
    ) {
        group.setId(groupId);
        if (StringUtils.isBlank(group.getName())) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, GROUP_NAME_IS_EMPTY);
            redirectAttributes.addFlashAttribute("changeGroup", group);
        } else {
            group.setName(group.getName().trim());
            try {
                decisionService.updateGroup(group);
            } catch (GroupAlreadyExistsByNameException e) {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, GROUP_WITH_SAME_NAME_ALREADY_EXISTS);
                redirectAttributes.addFlashAttribute("changeGroup", group);
            }
        }
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_CHANGED);
        return "redirect:/decisions";
    }

    /**
     * Удаление группы
     *
     * @param groupId - Идентификатор группы
     */
    @DeleteMapping("/decisions/group/{groupId}")
    public String groupDelete(
        @PathVariable Long groupId,
        RedirectAttributes redirectAttributes
    ) {
        decisionService.deleteGroupById(groupId);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_DELETED);
        return "redirect:/decisions";
    }

    /**
     * Добавление решение
     *
     * @param decision - Решение
     */
    @PostMapping("/decisions")
    public String addDecision(
        @RequestBody DecisionWithGroup decision,
        RedirectAttributes redirectAttributes
    ) {
        if (StringUtils.isBlank(decision.getName())) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, DECISION_NAME_IS_EMPTY);
        } else {
            try {
                decisionService.addDecision(decision);
            } catch (DecisionAlreadyExistsByNameException e) {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, DECISION_WITH_SAME_NAME_ALREADY_EXISTS);
            }
            redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_ADDED);
        }
        return "redirect:/decisions";
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

    /**
     * Изменение решения
     *
     * @param decisionId - Идентификатор решения
     * @param decision   - Решение
     */
    @PostMapping("/decisions/{decisionId}")
    public String decisionUpdate(
        @PathVariable Long decisionId,
        @RequestBody DecisionWithGroup decision,
        RedirectAttributes redirectAttributes
    ) {
        decision.setId(decisionId);
        if (StringUtils.isBlank(decision.getName())) {
            redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, DECISION_NAME_IS_EMPTY);
            redirectAttributes.addFlashAttribute("changeDecision", decision);
            return "redirect:/decisions";
        } else {
            try {
                decisionService.updateDecision(decision);
                redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_CHANGED);
                return "redirect:/decisions";
            } catch (DecisionAlreadyExistsByNameException e) {
                redirectAttributes.addFlashAttribute(ERROR_MESSAGE_PARAM, DECISION_WITH_SAME_NAME_ALREADY_EXISTS);
                redirectAttributes.addFlashAttribute("changeDecision", decision);
                return "redirect:/decisions";
            }
        }
    }


    /**
     * Удаление решения
     *
     * @param decisionId - Идентификатор решения
     */
    @DeleteMapping("/decisions/{decisionId}")
    public String deleteDecision(
        @PathVariable Long decisionId,
        RedirectAttributes redirectAttributes
    ) {
        decisionService.deleteDecisionById(decisionId);
        redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_DELETED);
        return "redirect:/decisions";
    }

}
