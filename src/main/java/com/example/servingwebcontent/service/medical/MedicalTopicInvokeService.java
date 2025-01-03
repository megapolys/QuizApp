package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.result.MedicalInvokeTopicDto;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResultUpdateCommandDto;

public interface MedicalTopicInvokeService {

	/**
	 * Назначение пользователю топик анализов на выполнение (создание нового результата топика анализов)
	 *
	 * @param userId  идентификатор пользователя, которому назначается тест
	 * @param topicId идентификатор топика
	 */
	void createNewMedicalResult(Long userId, Long topicId);

	/**
	 * Получение списка тасков для топика анализов
	 *
	 * @param userId        идентификатор пользователя, который запрашивает тест
	 * @param topicResultId идентификатор результата топика анализов
	 *
	 * @return список тасков
	 */
	MedicalInvokeTopicDto getMedicalTopicResult(Long topicResultId, Long userId);

	/**
	 * Сохранить результат топика анализов
	 *
	 * @param userId  идентификатор пользователя, который вносит изменения
	 * @param command команда с результатами
	 */
	void saveMedicalTopicResult(MedicalTopicResultUpdateCommandDto command, Long userId);
}
