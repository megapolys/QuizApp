package com.example.servingwebcontent.controller.medical;

import com.example.servingwebcontent.model.medical.MedicalTaskCreateCommandDto;
import com.example.servingwebcontent.model.medical.MedicalTaskFull;
import com.example.servingwebcontent.model.medical.MedicalTaskWithDecisionsSize;
import com.example.servingwebcontent.service.medical.MedicalTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalTaskRestController {

	private final MedicalTaskService medicalTaskService;

	/**
	 * Создание нового анализа
	 *
	 * @param command команда для создания
	 */
	@PostMapping("api/medical/task")
	void createMedicalTask(@Valid @RequestBody MedicalTaskCreateCommandDto command) {
		medicalTaskService.createMedicalTask(command);
	}

	/**
	 * Получение списка анализов
	 *
	 * @param medicalTopicId идентификатор топика анализов
	 *
	 * @return список анализов
	 */
	@GetMapping(value = "api/medical/task/all", params = "medicalTopicId")
	List<MedicalTaskWithDecisionsSize> getMedicalTaskList(@RequestParam("medicalTopicId") Long medicalTopicId) {
		return medicalTaskService.getMedicalTaskList(medicalTopicId);
	}

	/**
	 * Получение анализа
	 *
	 * @param medicalTaskId идентификатор анализа
	 *
	 * @return анализа
	 */
	@GetMapping(value = "api/medical/task/{medicalTaskId}")
	MedicalTaskFull getMedicalTaskById(@PathVariable("medicalTaskId") Long medicalTaskId) {
		return medicalTaskService.getMedicalTaskById(medicalTaskId);
	}
}
