package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.model.validation.TaskForm;
import com.example.servingwebcontent.model.validation.TaskType;
import com.example.servingwebcontent.service.decision.DecisionService;
import com.example.servingwebcontent.service.quiz.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class QuizController {

	private final QuizService quizService;
	private final DecisionService decisionService;

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return quizList - список тестов
	 */
	@GetMapping("/quiz/list")
	public String getQuizList(Model model) {
		model.addAttribute("quizTab", "active");
		return "quiz/quizList";
	}

	/**
	 * Получение теста
	 *
	 * @return quizList - список тестов,
	 * quizTab - активность вкладки в заголовках
	 */
	@GetMapping("/quiz/{quizId}")
	public String quiz(@PathVariable Long quizId, Model model) {
		model.addAttribute("quiz", quizService.getQuiz(quizId));
		model.addAttribute("taskList", quizService.getTaskListByQuiz(quizId));
		model.addAttribute("groups", decisionService.groups());
		model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
		if (model.asMap().get("taskForm") != null) {
			this.taskForm.setFromTaskForm((TaskForm) model.asMap().get("taskForm"));
		}
		this.taskForm.setPosition(quizService.getNextTaskPosition(quiz));
		model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
		model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
		model.addAttribute("quizTab", "active");
		return "quiz/quiz";
	}

	@PostMapping("/quiz/rename/{quiz}")
	public String renameQuiz(
		@RequestParam String shortName,
		@RequestParam String name,
		@PathVariable QuizWithTaskSize quiz,
		RedirectAttributes redirectAttributes
	) {
//		addOrUpdateQuiz(shortName, name, redirectAttributes, quiz, true);
		return "redirect:/quiz/{quiz}";
	}

	@PostMapping("/quiz/delete/{quiz}")
	public String deleteQuiz(@PathVariable QuizWithTaskSize quiz) {
//		quizService.delete(quiz);
		return "redirect:/quiz/list";
	}

}
