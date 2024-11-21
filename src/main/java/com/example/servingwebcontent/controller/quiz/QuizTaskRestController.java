package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizTaskRestController {

	private final QuizService quizService;

	/**
	 * Получить список вопросов
	 *
	 * @param quizId идентификатор теста
	 *
	 * @return список вопросов
	 */
	@GetMapping(value = "/api/quiz/task", params = "quizId")
	public List<QuizTask> getQuizTaskList(@RequestParam Long quizId) {
		return quizService.getQuizTaskList(quizId);
	}
}
