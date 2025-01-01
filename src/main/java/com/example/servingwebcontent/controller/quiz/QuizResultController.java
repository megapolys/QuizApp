package com.example.servingwebcontent.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class QuizResultController {

	@GetMapping("quiz/result/{userId}")
	public String getQuizList(
		@PathVariable Long userId,
		Model model
	) {
		model.addAttribute("userId", userId);
		model.addAttribute("usersTab", "active");
		return "quiz/result/quizList";
	}

	@GetMapping("quiz/result/{userId}/{quizResultId}")
	public String getQuizResult(
		@PathVariable Long userId,
		@PathVariable Long quizResultId,
		Model model
	) {
		model.addAttribute("quizResultId", quizResultId);
		model.addAttribute("userId", userId);
		model.addAttribute("usersTab", "active");
		return "quiz/result/quiz";
	}

	@GetMapping("quiz/result/{userId}/{quizResultId}/{quizTaskResultId}")
	public String getQuizTaskResult(
		@PathVariable Long userId,
		@PathVariable Long quizResultId,
		@PathVariable Long quizTaskResultId,
		Model model
	) {
		model.addAttribute("userId", userId);
		model.addAttribute("quizResultId", quizResultId);
		model.addAttribute("quizTaskResultId", quizTaskResultId);
		model.addAttribute("usersTab", "active");
		return "quiz/result/quizTask";
	}
}
