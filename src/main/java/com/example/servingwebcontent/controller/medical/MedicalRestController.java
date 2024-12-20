package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.MedicalTopic;
import com.example.servingwebcontent.model.medical.MedicalTopicCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTopicUpdateCommandDto;
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
	 * Получение топика анализа по идентификатору
	 *
	 * @param id идентификатор анализа
	 *
	 * @return анализ
	 */
	@GetMapping("api/medical/{id}")
	MedicalTopic getMedicalTopic(@PathVariable("id") Long id) {
		return medicalTopicService.getMedicalTopic(id);
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
	 * Изменение анализа
	 *
	 * @param command команда для изменения
	 */
	@PutMapping("api/medical")
	void updateMedicalTopic(@Valid @RequestBody MedicalTopicUpdateCommandDto command) {
		medicalTopicService.updateMedicalTopic(command);
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

	/**
	 * Создание глубокой копии анализа с новым наименованием
	 *
	 * @param id идентификатор анализа
	 */
	@PostMapping("api/medical/clone/{id}")
	void cloneMedicalTopic(@PathVariable Long id) {
		medicalTopicService.cloneMedicalTopic(id);
	}
}
