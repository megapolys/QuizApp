package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.quiz.task.TaskCreateCommandDto;
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
	 * Удаление вопроса
	 *
	 * @param taskId идентификатор вопроса
	 */
	@DeleteMapping(value = "/api/quiz/task/{taskId}")
	public void deleteQuizTaskById(@PathVariable Long taskId) {
		quizTaskService.deleteQuizTask(taskId);
	}

	@PostMapping(value = "/api/quiz/task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void createTask(
			@RequestPart("taskCreateCommand") @Valid TaskCreateCommandDto taskCreateCommand,
			@RequestPart("file") MultipartFile file
	) {

	}
}
