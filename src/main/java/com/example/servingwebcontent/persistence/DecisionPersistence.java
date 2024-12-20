package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;

import java.util.List;
import java.util.Optional;

public interface DecisionPersistence {

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
	 * Проверка наличия группы с данным именем
	 *
	 * @param name - имя группы
	 *
	 * @return true - если группа найдена, false в обратном случае
	 */
	boolean existsGroupByName(String name);

	/**
	 * Создание новой группы
	 *
	 * @param group - Группа
	 */
	void createGroup(Group group);

	/**
	 * Получение группы по идентификатору
	 *
	 * @param groupId - Идентификатор группы
	 *
	 * @return Группа
	 */
	Group getGroupById(Long groupId);

	/**
	 * Получение группы по имени
	 *
	 * @param name - Имя группы
	 *
	 * @return Группа
	 */
	Optional<Group> findGroupByName(String name);

	/**
	 * Изменение группы
	 *
	 * @param group - Группа
	 */
	void updateGroup(Group group);

	/**
	 * Удаление группы (без удаления вложенных решений)
	 *
	 * @param groupId - Идентификатор группы
	 */
	void deleteGroupById(Long groupId);

	/**
	 * Проверка наличия решения с данным именем
	 *
	 * @param name - Имя решения
	 *
	 * @return true - Если решение найдена, false в обратном случае
	 */
	boolean existsDecisionsByName(String name);

	/**
	 * Создание нового решения
	 *
	 * @param decision - Решение
	 */
	void createDecision(DecisionWithGroup decision);

	/**
	 * Получение решения
	 *
	 * @param decisionId - Идентификатор решения
	 *
	 * @return Решение
	 */
	DecisionWithGroup getDecisionById(Long decisionId);

	/**
	 * Получение решения по имени
	 *
	 * @param name - Имя решения
	 *
	 * @return Решение
	 */
	Optional<Decision> findDecisionByName(String name);

	/**
	 * Обновление решения
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
