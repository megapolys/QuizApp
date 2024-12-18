package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.MedicalTaskCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTaskFull;
import com.example.servingwebcontent.model.medical.MedicalTaskWithDecisionsSize;

import java.util.List;

public interface MedicalTaskService {

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	void createMedicalTask(MedicalTaskCreateCommandDto command);

	/**
	 * Получение списка анализов
	 *
	 * @param medicalTopicId идентификатор топика анализов
	 *
	 * @return список анализов
	 */
	List<MedicalTaskWithDecisionsSize> getMedicalTaskList(Long medicalTopicId);

	/**
	 * Получение анализа
	 *
	 * @param medicalTaskId идентификатор анализа
	 *
	 * @return анализа
	 */
	MedicalTaskFull getMedicalTaskById(Long medicalTaskId);
}
