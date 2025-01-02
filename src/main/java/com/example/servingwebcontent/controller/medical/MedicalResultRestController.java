package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.result.MedicalWithResults;
import com.example.servingwebcontent.service.medical.MedicalTopicResultService;
import com.example.servingwebcontent.service.medical.impl.MedicalTopicInvokeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalResultRestController {

	private final MedicalTopicResultService medicalTopicResultService;
	private final MedicalTopicInvokeServiceImpl medicalTopicInvokeService;

	/**
	 * Получить список результатов анализов, назначенных пользователю
	 *
	 * @param userId идентификатор пользователя
	 *
	 * @return список результатов анализов
	 */
	@GetMapping(value = "api/medical/result", params = "userId")
	public List<MedicalWithResults> getMedicalResultList(@RequestParam Long userId) {
		return medicalTopicResultService.getMedicalResultListByUserId(userId);
	}

	/**
	 * Удаление результата анализов по идентификатору
	 *
	 * @param medicalResultId идентификатор результата анализов
	 */
	@DeleteMapping(value = "api/medical/result/{medicalResultId}")
	public void deleteMedicalResultById(@PathVariable Long medicalResultId) {
		medicalTopicResultService.deleteMedicalResultById(medicalResultId);
	}

	/**
	 * Назначение пользователю топик анализов на выполнение (создание нового результата топика анализов)
	 *
	 * @param userId  идентификатор пользователя, которому назначается тест
	 * @param topicId идентификатор топика
	 */
	@PostMapping(value = "api/medical/result", params = {"userId", "topicId"})
	public void createNewMedicalResult(
		@RequestParam Long userId,
		@RequestParam Long topicId
	) {
		medicalTopicInvokeService.createNewMedicalResult(userId, topicId);
	}
}
