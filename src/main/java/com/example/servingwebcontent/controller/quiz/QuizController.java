package com.example.servingwebcontent.controller.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class QuizController {

	/**
	 * Получение страницы списка тестов
	 *
	 * @return quizList - список тестов
	 */
	@GetMapping("/quiz/list")
	public String getQuizList(Model model) {
		model.addAttribute("quizTab", "active");
		return "quiz/quizList";
	}

	/**
	 * Получение страница с тестом
	 *
	 * @return quizTab - активность вкладки в заголовках
	 */
	@GetMapping("/quiz/{quizId}")
	public String quiz(@PathVariable Long quizId, Model model) {
//		model.addAttribute("quiz", quizService.getQuiz(quizId));
//		model.addAttribute("taskList", quizService.getTaskListByQuiz(quizId));
//		model.addAttribute("groups", decisionService.groups());
//		model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
//		if (model.asMap().get("taskForm") != null) {
//			this.taskForm.setFromTaskForm((TaskForm) model.asMap().get("taskForm"));
//		}
//		this.taskForm.setPosition(quizService.getNextTaskPosition(quiz));
//		model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
//		model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
//		model.addAttribute("quizId", quizId);
		model.addAttribute("quizTab", "active");
		return "quiz/quiz";
	}
}
