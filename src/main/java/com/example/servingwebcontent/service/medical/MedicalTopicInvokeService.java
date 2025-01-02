package com.example.servingwebcontent.service.medical;

public interface MedicalTopicInvokeService {

	/**
	 * Назначение пользователю топик анализов на выполнение (создание нового результата топика анализов)
	 *
	 * @param userId  идентификатор пользователя, которому назначается тест
	 * @param topicId идентификатор топика
	 */
	void createNewMedicalResult(Long userId, Long topicId);
}
