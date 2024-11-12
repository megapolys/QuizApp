package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;
import com.example.servingwebcontent.service.decision.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.servingwebcontent.consts.Consts.SUCCESS_MESSAGE_PARAM;
import static com.example.servingwebcontent.consts.MessageConsts.DECISION_SUCCESSFUL_DELETED;
import static com.example.servingwebcontent.consts.MessageConsts.GROUP_SUCCESSFUL_DELETED;

@RestController
@RequiredArgsConstructor
public class DecisionRestController {

	private final DecisionService decisionService;

	/**
	 * Получение всех решений вместе с группами
	 *
	 * @return Группы с решениями
	 */
	@GetMapping("/api/decisions")
	public List<GroupWithDecisions> getDecisions() {
		return decisionService.getDecisionGroups();
	}

	/**
	 * Удаление группы
	 *
	 * @param groupId - Идентификатор группы
	 */
	@DeleteMapping("/api/decisions/group/{groupId}")
	public String groupDelete(
			@PathVariable Long groupId,
			RedirectAttributes redirectAttributes
	) {
		decisionService.deleteGroupById(groupId);
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, GROUP_SUCCESSFUL_DELETED);
		return "redirect:/decisions";
	}

	/**
	 * Удаление решения
	 *
	 * @param decisionId - Идентификатор решения
	 */
	@DeleteMapping("/api/decisions/{decisionId}")
	public String deleteDecision(
			@PathVariable Long decisionId,
			RedirectAttributes redirectAttributes
	) {
		decisionService.deleteDecisionById(decisionId);
		redirectAttributes.addFlashAttribute(SUCCESS_MESSAGE_PARAM, DECISION_SUCCESSFUL_DELETED);
		return "redirect:/decisions";
	}

	/**
	 * Добавление решение
	 *
	 * @param decision - Решение
	 */
	@PostMapping(value = "/api/decisions")
	public void addDecision(
			@RequestBody DecisionWithGroup decision
	) {
		decisionService.addDecision(decision);
	}
}
