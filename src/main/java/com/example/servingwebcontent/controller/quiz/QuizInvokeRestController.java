package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizTaskCompleteCommand;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import com.example.servingwebcontent.model.quiz.result.QuizWithResults;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QuizInvokeRestController {

	private final QuizInvokeService quizInvokeService;
	private final QuizResultService quizResultService;


	/**
	 * Получение списка тестов, назначенных пользователю к исполнению
	 *
	 * @return список результатов тестов
	 */
	@GetMapping(value = "api/quiz/invoke")
	public List<QuizWithResults> getQuizResultList() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		return quizResultService.getQuizResultListByUserId(userDetails.getId());
	}

	/**
	 * Получение списка вопросов по тесту
	 *
	 * @param quizResultId идентификатор результата теста
	 *
	 * @return список вопросов
	 */
	@GetMapping(value = "api/quiz/task/invoke")
	public List<QuizTaskResultWithTaskType> getQuizTaskListByUserId(@RequestParam Long quizResultId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		return quizInvokeService.getQuizTaskListByQuizResultId(userDetails.getId(), quizResultId);
	}

	/**
	 * Сохранить результат ответа
	 *
	 * @param command команда с результатом ответа
	 */
	@PostMapping("api/quiz/task/invoke")
	public void saveTaskResult(@Valid @RequestBody QuizTaskCompleteCommand command) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
		quizInvokeService.saveTaskResult(userDetails.getId(), command);
	}

}
