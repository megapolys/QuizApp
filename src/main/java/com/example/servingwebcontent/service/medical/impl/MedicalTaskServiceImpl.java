package com.example.servingwebcontent.service.medical.impl;

import com.example.servingwebcontent.exceptions.medical.MedicalTaskAlreadyExistsException;
import com.example.servingwebcontent.exceptions.medical.MedicalTaskInvalidException;
import com.example.servingwebcontent.model.medical.MedicalTask;
import com.example.servingwebcontent.model.medical.MedicalTaskCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.persistence.MedicalPersistence;
import com.example.servingwebcontent.service.medical.MedicalTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalTaskServiceImpl implements MedicalTaskService {

	private final MedicalPersistence medicalPersistence;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createMedicalTask(MedicalTaskCreateCommandDto command) {
		validateMedicalTask(command);
		MedicalTask medicalTaskByName = medicalPersistence.findMedicalTaskByName(command.getName());
		if (medicalTaskByName != null) {
			throw MedicalTaskAlreadyExistsException.byName(command.getName());
		}
		medicalPersistence.createMedicalTask(command);
	}

	private void validateMedicalTask(MedicalTaskCreateCommandDto command) {
		if (!(command.getLeftLeft() < command.getLeftMid() &&
			command.getLeftMid() < command.getRightMid() &&
			command.getRightMid() < command.getRightRight())) {
			throw MedicalTaskInvalidException.byReference();
		}
	}

	public void deleteTask(MedicalTask task) {
//        medicalTaskRepository.delete(task);
	}

	public boolean updateTask(MedicalTopicWithTaskSize topic, MedicalTask task) {
//        if (topic.getMedicalTasks().stream().anyMatch(t -> t.getName().equals(task.getName()) && !Objects.equals(t.getId(), task.getId()))) {
//            return false;
//        } else {
//            medicalTaskRepository.save(task);
//            return true;
//        }
		return false;
	}
}
