package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizResultRestController {

	private final QuizResultService quizResultService;

	/**
	 * Получение списка результатов тестов для пользователя
	 *
	 * @param userId идентификатор пользователя
	 *
	 * @return список результатов тестов
	 */
	@GetMapping(value = "api/quiz/result", params = "userId")
	public List<QuizResult> getQuizResultListByUserId(@RequestParam Long userId) {
		return quizResultService.getQuizResultListByUserId(userId);
	}
}
