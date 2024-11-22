package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.service.quiz.QuizTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
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

	@DeleteMapping(value = "/api/quiz/task/{taskId}")
	public void deleteQuizTaskById(@PathVariable Long taskId) {
		quizTaskService.deleteQuizTask(taskId);
	}
}
