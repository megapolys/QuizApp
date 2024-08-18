package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import com.example.servingwebcontent.service.decision.impl.DecisionServiceImpl;
import com.example.servingwebcontent.service.quiz.impl.QuizServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class QuizController {

	private final QuizServiceImpl quizService;
	private final DecisionServiceImpl decisionService;

	/**
	 * Получение списка тестов, сортированных по shortName
	 *
	 * @return quizList - список тестов,
	 * quizTab - активность вкладки в заголовках
	 */
	@GetMapping("/quiz/list")
	public String getQuizList(Model model) {
		model.addAttribute("quizList", quizService.getQuizList());
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
//        model.addAttribute("quiz", quizService.getQuiz(quizId));
//        model.addAttribute("taskList", quizService.getTaskListByQuiz(quizId));
//        model.addAttribute("groups", decisionService.groups());
//        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
//        if (model.asMap().get("taskForm") != null) {
//            this.taskForm.setFromTaskForm((TaskForm) model.asMap().get("taskForm"));
//        }
//        this.taskForm.setPosition(quizService.getNextTaskPosition(quiz));
//        model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
//        model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
//        model.addAttribute("quizTab", "active");
		return "quiz/quiz";
	}

	@PostMapping("/quiz/add")
	public String addQuiz(
		@RequestParam String shortName,
		@RequestParam String name,
		RedirectAttributes redirectAttributes
	) {
//        Quiz quiz = new Quiz();
//        addOrUpdateQuiz(shortName, name, redirectAttributes, quiz, false);
		return "redirect:/quiz/list";
	}

	@PostMapping("/quiz/rename/{quiz}")
	public String renameQuiz(
		@RequestParam String shortName,
		@RequestParam String name,
		@PathVariable QuizWithTaskSize quiz,
		RedirectAttributes redirectAttributes
	) {
		addOrUpdateQuiz(shortName, name, redirectAttributes, quiz, true);
		return "redirect:/quiz/{quiz}";
	}

	@PostMapping("/quiz/delete/{quiz}")
	public String deleteQuiz(@PathVariable QuizWithTaskSize quiz) {
		quizService.delete(quiz);
		return "redirect:/quiz/list";
	}

	private void addOrUpdateQuiz(@RequestParam String shortName, @RequestParam String name, RedirectAttributes redirectAttributes, QuizWithTaskSize quiz, boolean update) {
		if (!StringUtils.hasText(name)) {
			redirectAttributes.addFlashAttribute("message", "Наименование теста не может быть пустым!");
		} else if (!StringUtils.hasText(shortName)) {
			redirectAttributes.addFlashAttribute("message", "Индекс не может быть пустым!");
		} else if (shortName.length() > 5) {
			redirectAttributes.addFlashAttribute("message", "Индекс должен быть не длиннее 5 символов");
		} else {
			quiz.setShortName(shortName);
			quiz.setName(name);
			QuizServiceImpl.QuizResult result = quizService.save(quiz);
			if (result.result() == QuizServiceImpl.ResultType.NAME_FOUND) {
				redirectAttributes.addFlashAttribute("message", "Тест с таким именем уже существует! Придумывай новое");
			}
			if (result.result() == QuizServiceImpl.ResultType.SHORT_NAME_FOUND) {
				redirectAttributes.addFlashAttribute("message", "Тест с таким индексом уже существует! Придумывай новый");
			}
			if (result.result() == QuizServiceImpl.ResultType.SUCCESS) {
				redirectAttributes.addFlashAttribute("successMessage", String.format("Опросник с '%s. %s' успешно %s",
					result.quiz().getShortName(), result.quiz().getName(), update ? "обновлен" : "добавлен"
				));
			}
		}
    }

}
