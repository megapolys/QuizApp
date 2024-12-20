package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.exceptions.medical.MedicalTaskAlreadyExistsException;
import com.example.servingwebcontent.exceptions.medical.MedicalTaskInvalidException;
import com.example.servingwebcontent.model.medical.*;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.service.medical.MedicalTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalTaskServiceImpl implements MedicalTaskService {

	private final MedicalPersistence medicalPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMedicalTask(MedicalTaskCreateCommandDto command) {
		validateMedicalTaskReferenceAndOptimumValues(command.getLeftLeft(), command.getLeftMid(), command.getRightMid(), command.getRightRight());
		MedicalTask medicalTaskByName = medicalPersistence.findMedicalTaskByName(command.getName(), command.getTopicId());
		if (medicalTaskByName != null) {
			throw MedicalTaskAlreadyExistsException.byName(command.getName());
		}
		medicalPersistence.createMedicalTask(command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateMedicalTask(MedicalTaskUpdateCommandDto command) {
		validateMedicalTaskReferenceAndOptimumValues(command.getLeftLeft(), command.getLeftMid(), command.getRightMid(), command.getRightRight());
		MedicalTask medicalTaskByName = medicalPersistence.findMedicalTaskByName(command.getName(), command.getTopicId());
		if (medicalTaskByName != null && !medicalTaskByName.getId().equals(command.getTaskId())) {
			throw MedicalTaskAlreadyExistsException.byName(command.getName());
		}
		medicalPersistence.updateMedicalTask(command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MedicalTaskWithDecisionsSize> getMedicalTaskList(Long medicalTopicId) {
		return medicalPersistence.getMedicalTaskList(medicalTopicId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MedicalTaskFull getMedicalTaskById(Long medicalTaskId) {
		return medicalPersistence.getMedicalTaskFullById(medicalTaskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteMedicalTaskById(Long medicalTaskId) {
		medicalPersistence.deleteMedicalTask(medicalTaskId);
	}

	private void validateMedicalTaskReferenceAndOptimumValues(Float leftLeft, Float leftMid, Float rightMid, Float rightRight) {
		if (!(leftLeft < leftMid && leftMid < rightMid && rightMid < rightRight)) {
			throw MedicalTaskInvalidException.byReference();
		}
	}

}
