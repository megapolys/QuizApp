package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.result.MedicalInvokeTopicDto;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResultUpdateCommandDto;
import com.example.servingwebcontent.model.medical.result.MedicalWithResults;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.service.medical.MedicalTopicInvokeService;
import com.example.servingwebcontent.service.medical.MedicalTopicResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalInvokeRestController {

	private final MedicalTopicResultService medicalTopicResultService;
	private final MedicalTopicInvokeService medicalTopicInvokeService;

	/**
	 * Получить список результатов анализов, назначенных пользователю
	 *
	 * @return список результатов анализов
	 */
	@GetMapping("api/medical/invoke")
	public List<MedicalWithResults> getMedicalResultList() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		return medicalTopicResultService.getMedicalResultListByUserId(userDetails.getId());
	}

	/**
	 * Получение списка тасков для топика анализов
	 *
	 * @param topicResultId идентификатор результата топика анализов
	 *
	 * @return список тасков
	 */
	@GetMapping("api/medical/invoke/{topicResultId}")
	public MedicalInvokeTopicDto getMedicalTopicResult(@PathVariable Long topicResultId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		return medicalTopicInvokeService.getMedicalTopicResult(topicResultId, userDetails.getId());
	}

	/**
	 * Сохранить результат топика анализов
	 *
	 * @param command команда с результатами
	 */
	@PostMapping("api/medical/invoke")
	public void saveMedicalTopicResult(@Valid @RequestBody MedicalTopicResultUpdateCommandDto command) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		medicalTopicInvokeService.saveMedicalTopicResult(command, userDetails.getId());
	}

}
