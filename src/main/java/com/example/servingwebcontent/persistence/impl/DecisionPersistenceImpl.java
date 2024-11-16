package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.DecisionEntityToDecisionConverter;
import com.example.servingwebcontent.converters.DecisionEntityToDecisionWithGroupConverter;
import com.example.servingwebcontent.converters.DecisionGroupEntityToGroupConverter;
import com.example.servingwebcontent.converters.DecisionGroupEntityToGroupWithDecisionsConverter;
import com.example.servingwebcontent.exceptions.decision.DecisionNotFoundException;
import com.example.servingwebcontent.exceptions.decision.GroupNotFoundException;
import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import com.example.servingwebcontent.persistence.DecisionPersistence;
import com.example.servingwebcontent.repositories.DecisionGroupRepository;
import com.example.servingwebcontent.repositories.DecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DecisionPersistenceImpl implements DecisionPersistence {

	private final DecisionRepository decisionRepository;
	private final DecisionGroupRepository decisionGroupRepository;

	private final DecisionEntityToDecisionConverter decisionEntityToDecisionConverter;
	private final DecisionGroupEntityToGroupWithDecisionsConverter groupWithDecisionsConverter;
	private final DecisionGroupEntityToGroupConverter groupConverter;
	private final DecisionEntityToDecisionWithGroupConverter decisionWithGroupConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Decision> getUngroupedDecisions() {
		return decisionRepository.findAllByGroupIdIsNullOrderByName().stream()
			.map(decisionEntityToDecisionConverter::convert)
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GroupWithDecisions> getDecisionGroups() {
		Map<Long, List<DecisionEntity>> decisionsByGroupId = decisionRepository.findAllByGroupIdIsNotNullOrderByName().stream()
			.collect(Collectors.groupingBy(DecisionEntity::getGroupId));
		return decisionGroupRepository.findAllByOrderByName().stream()
			.map(groupEntity -> groupWithDecisionsConverter.convert(groupEntity, decisionsByGroupId.get(groupEntity.getId())))
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsGroupByName(String name) {
		return decisionGroupRepository.existsByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createGroup(Group group) {
		decisionGroupRepository.save(DecisionGroupEntity.createNew(group.getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Group getGroupById(Long groupId) {
		return decisionGroupRepository.findById(groupId)
			.map(groupConverter::convert)
			.orElseThrow(() -> GroupNotFoundException.byId(groupId));
	}

	@Override
	public Optional<Group> findGroupByName(String name) {
		return decisionGroupRepository.findByName(name)
			.map(groupConverter::convert);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateGroup(Group group) {
		decisionGroupRepository.save(DecisionGroupEntity.buildExisting(
			group.getId(),
			group.getName()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteGroupById(Long groupId) {
		decisionRepository.deleteGroup(groupId);
		decisionGroupRepository.deleteById(groupId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsDecisionsByName(String name) {
		return decisionRepository.existsByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createDecision(DecisionWithGroup decision) {
		decisionRepository.save(DecisionEntity.createNew(
				decision.getName(),
				decision.getDescription(),
				decision.getGroupId()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DecisionWithGroup getDecisionById(Long decisionId) {
		return decisionRepository.findById(decisionId)
			.map(decisionWithGroupConverter::convert)
			.orElseThrow(() -> DecisionNotFoundException.byId(decisionId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Decision> findDecisionByName(String name) {
		return decisionRepository.findByName(name)
			.map(decisionEntityToDecisionConverter::convert);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDecision(DecisionWithGroup decision) {
		decisionRepository.save(DecisionEntity.buildExisting(
			decision.getId(),
			decision.getName(),
			decision.getDescription(),
			decision.getGroupId()
		));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteDecisionById(Long decisionId) {
		decisionRepository.deleteById(decisionId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Group> getGroups() {
		return decisionGroupRepository.findAllByOrderByName().stream()
			.map(groupConverter::convert)
			.toList();
	}
}
