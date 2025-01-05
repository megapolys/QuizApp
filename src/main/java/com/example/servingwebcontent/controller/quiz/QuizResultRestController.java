package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizFullResultDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultUpdateCommandDto;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import com.example.servingwebcontent.model.quiz.result.QuizWithResults;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizResultRestController {

	private final QuizResultService quizResultService;
	private final QuizInvokeService quizInvokeService;

	/**
	 * Получение списка результатов тестов для пользователя
	 *
	 * @param userId идентификатор пользователя
	 *
	 * @return список результатов тестов
	 */
	@GetMapping(value = "api/quiz/result", params = "userId")
	public List<QuizWithResults> getQuizResultListByUserId(@RequestParam Long userId) {
		return quizResultService.getQuizResultListByUserId(userId);
	}

	/**
	 * Получить полный результат теста по идентификатору результата теста
	 *
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return полный результат теста
	 */
	@GetMapping(value = "api/quiz/result/{quizResultId}")
	public QuizFullResultDto getQuizResultById(@PathVariable Long quizResultId) {
		return quizResultService.getQuizResultById(quizResultId);
	}

	/**
	 * Удаление результата теста по идентификатору
	 *
	 * @param quizResultId идентификатор результата теста
	 */
	@DeleteMapping(value = "api/quiz/result/{quizResultId}")
	public void deleteQuizResultById(@PathVariable Long quizResultId) {
		quizResultService.deleteQuizResultById(quizResultId);
	}

	/**
	 * Назначение пользователю теста на выполнение (создание нового результата теста)
	 *
	 * @param userId идентификатор пользователя, которому назначается тест
	 * @param quizId идентификатор теста
	 */
	@PostMapping(value = "api/quiz/result", params = {"userId", "quizId"})
	public void createNewQuizResult(
		@RequestParam Long userId,
		@RequestParam Long quizId
	) {
		quizInvokeService.createNewQuizResult(userId, quizId);
	}

	/**
	 * Получение результата теста
	 *
	 * @param quizTaskResultId идентификатор результата теста
	 *
	 * @return результат теста
	 */
	@GetMapping("api/quiz/task/result/{quizTaskResultId}")
	public QuizTaskResultWithTaskType getQuizTaskResultById(@PathVariable Long quizTaskResultId) {
		return quizResultService.getQuizTaskResultById(quizTaskResultId);
	}

	/**
	 * Изменить значение для результата вопроса
	 *
	 * @param command команда для обновления
	 */
	@PostMapping("api/quiz/task/result")
	public void updateQuizTaskResult(@Valid @RequestBody QuizTaskResultUpdateCommandDto command) {
		quizResultService.updateQuizTaskResult(command);
	}
}
