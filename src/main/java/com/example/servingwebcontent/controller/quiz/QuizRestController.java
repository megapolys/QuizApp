package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.service.decision.DecisionService;
import com.example.servingwebcontent.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizRestController {

	private final QuizService quizService;
	private final DecisionService decisionService;

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return Список тестов с размером
	 */
	@GetMapping("/api/quiz")
	public List<QuizWithTaskSize> getQuizList() {
		return quizService.getQuizList();
	}

}
