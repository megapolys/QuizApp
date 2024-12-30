package com.example.servingwebcontent.controller.quiz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuizInvokeController {

	@GetMapping("/quiz/invoke")
	public String getQuizInvokeListPage(
		Model model
	) {
		model.addAttribute("invokeQuizTab", "active");
		return "quiz/invoke/quizList";
	}

	@GetMapping("/quiz/invoke/{quizResultId}")
	public String invokeQuiz(
		@PathVariable Long quizResultId,
		Model model
	) {
		model.addAttribute("quizResultId", quizResultId);
		model.addAttribute("invokeQuizTab", "active");
		return "quiz/invoke/quiz";
	}

}
