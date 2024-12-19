package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.medical.*;

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

	/**
	 * Получение анализа по имени
	 *
	 * @param name наименование
	 *
	 * @return анализ, если не найден - null
	 */
	MedicalTask findMedicalTaskByName(String name, Long topicId);

	/**
	 * Создание анализа
	 *
	 * @param command команда для создания
	 */
	void createMedicalTask(MedicalTaskCreateCommandDto command);

	/**
	 * Обновление анализа
	 *
	 * @param command команда для обновления
	 */
	void updateMedicalTask(MedicalTaskUpdateCommandDto command);

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
	MedicalTaskFull getMedicalTaskFullById(Long medicalTaskId);

	/**
	 * Удаление анализа
	 *
	 * @param medicalTaskId идентификатор анализа
	 */
	void deleteMedicalTask(Long medicalTaskId);
}
