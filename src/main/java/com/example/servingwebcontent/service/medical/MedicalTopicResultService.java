package com.example.servingwebcontent.service.medical;

import com.example.servingwebcontent.model.medical.result.MedicalFullResultDto;
import com.example.servingwebcontent.model.medical.result.MedicalWithResults;

import java.util.List;

public interface MedicalTopicResultService {

	/**
	 * Получить список результатов анализов, назначенных пользователю
	 *
	 * @param userId идентификатор пользователя
	 *
	 * @return список результатов анализов
	 */
	List<MedicalWithResults> getMedicalResultListByUserId(Long userId);

	/**
	 * Удаление результата анализов по идентификатору
	 *
	 * @param medicalResultId идентификатор результата анализов
	 */
	void deleteMedicalResultById(Long medicalResultId);

	/**
	 * Получить полный результат анализов по идентификатору результата анализов
	 *
	 * @param medicalResultId идентификатор результата анализов
	 *
	 * @return полный результат анализов
	 */
	MedicalFullResultDto getMedicalResultById(Long medicalResultId);
}
