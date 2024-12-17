package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.MedicalTaskCreateCommandDto;
import com.example.servingwebcontent.service.medical.MedicalTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MedicalTaskRestController {

	private final MedicalTopicService medicalTopicService;

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	@PostMapping("api/medical/task")
	void createMedicalTask(@Valid @RequestBody MedicalTaskCreateCommandDto command) {
		medicalTopicService.createMedicalTask(command);
	}
}
