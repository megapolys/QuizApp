package com.example.servingwebcontent.service.decision;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;

import java.util.List;

public interface DecisionService {

	/**
	 * Получение решений без группы и сортированные по полю name
	 *
	 * @return Список решений
	 */
	List<Decision> getUngroupedDecisions();

	/**
	 * Получение групп решений сортированных по полю name
	 *
	 * @return Список групп решений
	 */
	List<GroupWithDecisions> getDecisionGroups();

	/**
	 * Добавление новой группы
	 */
	void addGroup(Group group);

	/**
	 * Получение группы по идентификатору
	 *
	 * @param groupId - Идентификатор группы
	 *
	 * @return Группа
	 */
	Group getGroupById(Long groupId);

	/**
	 * Изменение группы
	 */
	void updateGroup(Group group);

	/**
	 * Удаление группы (без удаления вложенных решений)
	 *
	 * @param groupId - Идентификатор группы
	 */
	void deleteGroupById(Long groupId);

	/**
	 * Добавление нового решения
	 */
	void addDecision(DecisionWithGroup decision);

	/**
	 * Получение решения
	 *
	 * @param decisionId - Идентификатор решения
	 *
	 * @return Решение
	 */
	DecisionWithGroup getDecisionById(Long decisionId);

	/**
	 * Изменение решения
	 *
	 * @param decision - Решение
	 */
	void updateDecision(DecisionWithGroup decision);

	/**
	 * Удаление решения
	 *
	 * @param decisionId - Идентификатор решения
	 */
	void deleteDecisionById(Long decisionId);

	/**
	 * Получение всех групп без решений
	 *
	 * @return Группы
	 */
	List<Group> getGroups();
}
