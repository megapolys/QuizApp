package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.QuizCreateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.service.decision.DecisionService;
import com.example.servingwebcontent.service.quiz.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

	/**
	 * Получение теста по идентификатору
	 *
	 * @param id - Идентификатор теста
	 *
	 * @return Тест
	 */
	@GetMapping("/api/quiz/{id}")
	public Quiz getQuiz(@PathVariable Long id) {
		return quizService.getQuiz(id);
	}

	/**
	 * Создание нового теста
	 *
	 * @param quiz - Новый тест
	 */
	@PostMapping("/api/quiz")
	public void addQuiz(@Valid @RequestBody QuizCreateCommandDto quiz) {
		quizService.addQuiz(quiz);
	}

	/**
	 * Изменение данных теста
	 *
	 * @param quiz - Тест
	 */
	@PutMapping("/api/quiz")
	public void updateQuiz(@Valid @RequestBody QuizUpdateCommandDto quiz) {
		quizService.updateQuiz(quiz);
	}

	/**
	 * Удаление теста
	 *
	 * @param id - Идентификатор теста
	 */
	@DeleteMapping("/api/quiz/{id}")
	public void deleteQuizById(@PathVariable Long id) {
		quizService.deleteQuizById(id);
	}
}
