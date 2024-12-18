package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.medical.MedicalTaskEntityToMedicalTaskFullConverter;
import com.example.servingwebcontent.exceptions.medical.MedicalTopicNotFoundException;
import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import com.example.servingwebcontent.model.entities.medical.decision.MedicalTaskLeftDecisionEntity;
import com.example.servingwebcontent.model.entities.medical.decision.MedicalTaskRightDecisionEntity;
import com.example.servingwebcontent.model.medical.*;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.repositories.medical.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
@Service
@RequiredArgsConstructor
public class MedicalPersistenceImpl implements MedicalPersistence {

	private final MedicalTopicRepository medicalTopicRepository;
	private final MedicalTopicResultRepository medicalTopicResultRepository;
	private final MedicalTaskRepository medicalTaskRepository;
	private final MedicalTaskResultRepository medicalTaskResultRepository;
	private final MedicalTaskLeftDecisionsRepository medicalTaskLeftDecisionsRepository;
	private final MedicalTaskRightDecisionsRepository medicalTaskRightDecisionsRepository;
	private final ConversionService conversionService;
	private final MedicalTaskEntityToMedicalTaskFullConverter medicalTaskFullConverter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MedicalTopicWithTaskSize> getMedicalTopicList() {
		return medicalTopicRepository.findAllByOrderByName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByName(String name) {
		return medicalTopicRepository.existsByName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMedicalTopic(MedicalTopicCreateCommandDto command) {
		medicalTopicRepository.save(MedicalTopicEntity.createNew(command.getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void deleteMedicalTopic(Long id) {
		medicalTaskRepository.findAllByTopicId(id)
			.forEach(task -> deleteMedicalTask(task.getId()));
		medicalTopicResultRepository.deleteAllByTopicId(id);
		medicalTopicRepository.deleteById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalTopic getMedicalTopic(Long id) {
		return medicalTopicRepository.findById(id)
			.map(medicalTopicEntity -> conversionService.convert(medicalTopicEntity, MedicalTopic.class))
			.orElseThrow(() -> MedicalTopicNotFoundException.byId(id));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void cloneMedicalTopic(Long id, String name) {
		MedicalTopicEntity newMedicalTopic = medicalTopicRepository.save(MedicalTopicEntity.createNew(name));
		medicalTaskRepository.findAllByTopicId(id)
			.stream().sorted(Comparator.comparing(MedicalTaskEntity::getId))
			.forEach(task -> {
				MedicalTaskEntity newTask = medicalTaskRepository.save(MedicalTaskEntity.createNew(
					task.getName(),
					task.getUnit(),
					newMedicalTopic.getId(),
					task.getLeftLeft(),
					task.getLeftMid(),
					task.getRightMid(),
					task.getRightRight()
				));
				medicalTaskLeftDecisionsRepository.findAllByMedicalTaskId(task.getId()).stream()
					.map(entity -> MedicalTaskLeftDecisionEntity.createNew(newTask.getId(), entity.getDecisionsId()))
					.forEach(medicalTaskLeftDecisionsRepository::save);
				medicalTaskRightDecisionsRepository.findAllByMedicalTaskId(task.getId()).stream()
					.map(entity -> MedicalTaskRightDecisionEntity.createNew(newTask.getId(), entity.getDecisionsId()))
					.forEach(medicalTaskRightDecisionsRepository::save);
			});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalTopic findByName(String name) {
		return medicalTopicRepository.findByName(name)
			.map(entity -> conversionService.convert(entity, MedicalTopic.class))
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMedicalTopic(MedicalTopicUpdateCommandDto command) {
		medicalTopicRepository.save(MedicalTopicEntity.buildExists(command.getId(), command.getName()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalTask findMedicalTaskByName(String name, Long topicId) {
		return medicalTaskRepository.findByNameAndTopicId(name, topicId)
			.map(taskEntity -> conversionService.convert(taskEntity, MedicalTask.class))
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMedicalTask(MedicalTaskCreateCommandDto command) {
		MedicalTaskEntity medicalTaskEntity = medicalTaskRepository.save(MedicalTaskEntity.createNew(
			command.getName(),
			command.getUnit(),
			command.getTopicId(),
			command.getLeftLeft(),
			command.getLeftMid(),
			command.getRightMid(),
			command.getRightRight()
		));
		command.getLeftDecisionIds()
			.forEach(decisionId -> medicalTaskLeftDecisionsRepository.save(
				MedicalTaskLeftDecisionEntity.createNew(medicalTaskEntity.getId(), decisionId))
			);
		command.getRightDecisionIds()
			.forEach(decisionId -> medicalTaskRightDecisionsRepository.save(
				MedicalTaskRightDecisionEntity.createNew(medicalTaskEntity.getId(), decisionId))
			);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MedicalTaskWithDecisionsSize> getMedicalTaskList(Long medicalTopicId) {
		return medicalTaskRepository.getMedicalTaskList(medicalTopicId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalTaskFull getMedicalTaskFullById(Long medicalTaskId) {
		MedicalTaskEntity medicalTaskEntity = medicalTaskRepository.findById(medicalTaskId)
			.orElse(null);
		List<Long> leftDecisions = medicalTaskLeftDecisionsRepository.findAllByMedicalTaskId(medicalTaskId).stream()
			.map(MedicalTaskLeftDecisionEntity::getDecisionsId)
			.toList();
		List<Long> rightDecisions = medicalTaskRightDecisionsRepository.findAllByMedicalTaskId(medicalTaskId).stream()
			.map(MedicalTaskRightDecisionEntity::getDecisionsId)
			.toList();
		return medicalTaskFullConverter.convert(medicalTaskEntity, leftDecisions, rightDecisions);
	}

	private void deleteMedicalTask(Long taskId) {
		medicalTaskResultRepository.deleteAllByTaskId(taskId);
		medicalTaskRepository.deleteById(taskId);
	}
}
