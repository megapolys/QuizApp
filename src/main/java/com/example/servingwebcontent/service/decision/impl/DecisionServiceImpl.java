package com.example.servingwebcontent.service.decision.impl;

import com.example.servingwebcontent.exceptions.DecisionAlreadyExistsByNameException;
import com.example.servingwebcontent.exceptions.GroupAlreadyExistsByNameException;
import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;
import com.example.servingwebcontent.persistence.DecisionPersistence;
import com.example.servingwebcontent.service.decision.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {

	private final DecisionPersistence decisionPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Decision> getUngroupedDecisions() {
		return decisionPersistence.getUngroupedDecisions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GroupWithDecisions> getDecisionGroups() {
		return decisionPersistence.getDecisionGroups();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addGroup(Group group) {
		group.setName(group.getName().trim());
		if (decisionPersistence.existsGroupByName(group.getName())) {
			throw GroupAlreadyExistsByNameException.byName(group.getName());
		}
		decisionPersistence.createGroup(group);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Group getGroupById(Long groupId) {
		return decisionPersistence.getGroupById(groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGroup(Group group) {
		Optional<Group> groupByName = decisionPersistence.findGroupByName(group.getName());
		if (groupByName.isPresent() && !Objects.equals(group.getId(), groupByName.get().getId())) {
			throw GroupAlreadyExistsByNameException.byName(group.getName());
		}
		decisionPersistence.updateGroup(group);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteGroupById(Long groupId) {
		decisionPersistence.deleteGroupById(groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addDecision(DecisionWithGroup decision) {
		decision.setName(decision.getName().trim());
		decision.setDescription(decision.getDescription() != null ? decision.getDescription().trim() : null);
		if (decisionPersistence.existsDecisionsByName(decision.getName())) {
			throw DecisionAlreadyExistsByNameException.byName(decision.getName());
		}
		decisionPersistence.createDecision(decision);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionWithGroup getDecisionById(Long decisionId) {
		return decisionPersistence.getDecisionById(decisionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDecision(DecisionWithGroup decision) {
		decision.setName(decision.getName().trim());
		decision.setDescription(decision.getDescription() != null ? decision.getDescription().trim() : null);
		Optional<Decision> decisionByName = decisionPersistence.findDecisionByName(decision.getName());
		if (decisionByName.isPresent() && !Objects.equals(decision.getId(), decisionByName.get().getId())) {
			throw DecisionAlreadyExistsByNameException.byName(decision.getName());
		}
		decisionPersistence.updateDecision(decision);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteDecisionById(Long decisionId) {
		decisionPersistence.deleteDecisionById(decisionId);
	}

}
