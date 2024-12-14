package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import com.example.servingwebcontent.service.medical.MedicalTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalRestController {

	private final MedicalTopicService medicalTopicService;

	/**
	 * Получение отсортированного списка топиков анализов
	 *
	 * @return список топиков анализов
	 */
	@GetMapping("api/medical/all")
	List<MedicalTopicWithTaskSize> getMedicalTopicList() {
		return medicalTopicService.getMedicalTopicList();
	}

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	@PostMapping("api/medical")
	void createMedicalTopic(@Valid @RequestBody MedicalTopicCreateCommandDto command) {
		medicalTopicService.createMedicalTopic(command);
	}

	/**
	 * Удаление анализа
	 *
	 * @param id идентификатор анализа
	 */
	@DeleteMapping("api/medical/{id}")
	void deleteMedicalTopic(@PathVariable Long id) {
		medicalTopicService.deleteMedicalTopic(id);
	}
}
