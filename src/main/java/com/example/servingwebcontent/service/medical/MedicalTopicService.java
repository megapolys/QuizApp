package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;

import java.util.List;

public interface MedicalTopicService {

	/**
	 * Получение отсортированного списка топиков анализов
	 *
	 * @return список топиков анализов
	 */
	List<MedicalTopicWithTaskSize> getMedicalTopicList();

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	void createMedicalTopic(MedicalTopicCreateCommandDto command);
}
