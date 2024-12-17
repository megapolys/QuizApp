package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.MedicalTaskCreateCommandDto;

public interface MedicalTaskService {

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	void createMedicalTask(MedicalTaskCreateCommandDto command);
}
