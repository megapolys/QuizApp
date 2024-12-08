package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
import com.example.servingwebcontent.model.quiz.task.TaskUpdateCommandDto;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class QuizTaskRestController {

	private final QuizTaskService quizTaskService;

	/**
	 * Получить список вопросов
	 *
	 * @param quizId идентификатор теста
	 *
	 * @return список вопросов
	 */
	@GetMapping(value = "/api/quiz/task", params = "quizId")
	public List<QuizTask> getQuizTaskList(@RequestParam Long quizId) {
		return quizTaskService.getQuizTaskList(quizId);
	}

	/**
	 * Получить данные вопроса
	 *
	 * @param taskId идентификатор вопроса
	 *
	 * @return вопрос
	 */
	@GetMapping(value = "/api/quiz/task", params = "taskId")
	public QuizTaskFull getQuizTaskById(@RequestParam("taskId") Long taskId) {
		return quizTaskService.getQuizTaskById(taskId);
	}

	/**
	 * Удаление вопроса
	 *
	 * @param taskId идентификатор вопроса
	 */
	@DeleteMapping(value = "/api/quiz/task/{taskId}")
	public void deleteQuizTaskById(@PathVariable Long taskId) {
		quizTaskService.deleteQuizTask(taskId);
	}

	/**
	 * Создать вопрос
	 *
	 * @param taskCreateCommand команда создания вопроса
	 * @param file              файл
	 */
	@PostMapping(value = "/api/quiz/task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void createTask(
		@RequestPart("taskCreateCommand") @Valid TaskCreateCommandDto taskCreateCommand,
		@RequestPart(value = "file", required = false) MultipartFile file
	) {
		quizTaskService.createTask(taskCreateCommand, file);
	}

	/**
	 * Изменение вопроса
	 *
	 * @param taskUpdateCommand команда для изменения вопроса
	 * @param file              файл
	 * @param taskId            идентификатор вопроса
	 */
	@PutMapping(value = "/api/quiz/task/{taskId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void updateTask(
		@RequestPart("taskUpdateCommand") @Valid TaskUpdateCommandDto taskUpdateCommand,
		@RequestPart(value = "file", required = false) MultipartFile file,
		@PathVariable("taskId") Long taskId
	) {
		quizTaskService.updateTask(taskUpdateCommand, file, taskId);
	}
}
