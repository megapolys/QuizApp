package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;
import com.example.servingwebcontent.service.decision.DecisionService;
import jakarta.validation.Valid;
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
	 * Получение всех решений вместе с группами
	 *
	 * @return Группы с решениями
	 */
	@GetMapping("/api/decisions/ungrouped")
	public List<Decision> getUngroupedDecisions() {
		return decisionService.getUngroupedDecisions();
	}

	/**
	 * Получение всех групп без решений
	 *
	 * @return Группы
	 */
	@GetMapping("/api/decisions/group")
	public List<Group> getGroups() {
		return decisionService.getGroups();
	}

	/**
	 * Получение группы по идентификатору
	 *
	 * @param groupId - Идентификатор группы
	 *
	 * @return Группа
	 */
	@GetMapping("/api/decisions/group/{groupId}")
	public Group getGroupById(@PathVariable Long groupId) {
		return decisionService.getGroupById(groupId);
	}

	/**
	 * Получение решения с группой по идентификатору
	 *
	 * @param decisionId - Идентификатор решения
	 *
	 * @return Решение с группой
	 */
	@GetMapping("/api/decisions/{decisionId}")
	public DecisionWithGroup getDecisionById(@PathVariable Long decisionId) {
		return decisionService.getDecisionById(decisionId);
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
	 * Добавление новой группы
	 *
	 * @param group - Решение
	 */
	@PostMapping("/api/decisions/group")
	public void addGroup(@Valid @RequestBody Group group) {
		decisionService.addGroup(group);
	}

	/**
	 * Изменение группы
	 *
	 * @param group - Группа
	 */
	@PutMapping("/api/decisions/group")
	public void groupUpdate(
		@Valid @RequestBody Group group
	) {
		group.setName(group.getName().trim());
		decisionService.updateGroup(group);
	}

	/**
	 * Добавление решение
	 *
	 * @param decision - Решение
	 */
	@PostMapping(value = "/api/decisions")
	public void addDecision(@Valid @RequestBody DecisionWithGroup decision) {
		decisionService.addDecision(decision);
	}

	/**
	 * Изменение решения
	 *
	 * @param decision - Решение
	 */
	@PutMapping("/api/decisions")
	public void decisionUpdate(@Valid @RequestBody DecisionWithGroup decision) {
		decisionService.updateDecision(decision);
	}
}
