package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;

import java.util.List;

public interface MedicalPersistence {

	/**
	 * Получение отсортированного списка топиков анализов
	 *
	 * @return список топиков анализов
	 */
	List<MedicalTopicWithTaskSize> getMedicalTopicList();

	/**
	 * Существует ли анализ с указанным наименованием
	 *
	 * @param name наименование
	 *
	 * @return true - если существует, иначе false
	 */
	boolean existsByName(String name);

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	void createMedicalTopic(MedicalTopicCreateCommandDto command);
}
