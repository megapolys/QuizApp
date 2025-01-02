package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.result.MedicalWithResults;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.service.medical.MedicalTopicResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalInvokeRestController {

	private final MedicalTopicResultService medicalTopicResultService;

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

}
