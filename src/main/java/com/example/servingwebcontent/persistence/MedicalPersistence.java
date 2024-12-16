package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicUpdateCommandDto;
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

	/**
	 * Удаление анализа
	 *
	 * @param id идентификатор анализа
	 */
	void deleteMedicalTopic(Long id);

	/**
	 * Получение анализа по идентификатору
	 *
	 * @param id идентификатор анализа
	 */
	MedicalTopic getMedicalTopic(Long id);

	/**
	 * Глубокое клонирование анализа с новым именем
	 *
	 * @param id   идентификатор анализа
	 * @param name новое имя
	 */
	void cloneMedicalTopic(Long id, String name);

	/**
	 * Получение топика по имени
	 *
	 * @param name наименование
	 *
	 * @return топик анализа
	 */
	MedicalTopic findByName(String name);

	/**
	 * Изменение анализа
	 *
	 * @param command команда для изменения
	 */
	void updateMedicalTopic(MedicalTopicUpdateCommandDto command);
}
