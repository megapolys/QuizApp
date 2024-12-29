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
		return "quiz/result/quizResult";
	}

//
//    @GetMapping("/{userId}/{quizResultId}/{quizTaskResult}")
//    public String getQuizResult(
//            @PathVariable Long userId,
//            @PathVariable Long quizResultId,
//            @PathVariable QuizTaskResult quizTaskResult,
//            Model model
//    ) {
//        model.addAttribute("task", quizTaskResult);
//        model.addAttribute("score", quizResultService.getWeight(quizTaskResult));
//        model.addAttribute("userId", userId);
//        model.addAttribute("quizResultId", quizResultId);
//        model.addAttribute("usersTab", "active");
//        return "result/quizTask";
//    }
//
//    @PostMapping("/{userId}/{quizResultId}/{quizTaskResult}")
//    public String saveTask(
//            @PathVariable Long userId,
//            @PathVariable Long quizResultId,
//            @PathVariable QuizTaskResult quizTaskResult,
//            @RequestParam Float altScore,
//            RedirectAttributes redirectAttributes
//    ) {
//        redirectAttributes.addAttribute("userId", userId);
//        redirectAttributes.addAttribute("quizResultId", quizResultId);
//        if (altScore != null) {
//            quizTaskResult.setAltScore(altScore);
//            quizInvokeService.saveTask(quizTaskResult);
//            redirectAttributes.addFlashAttribute("successMessage", "Изменение сохранено");
//            return "redirect:/result/quiz/{userId}/{quizResultId}";
//        } else {
//            redirectAttributes.addFlashAttribute("message", "Введите балл");
//            redirectAttributes.addAttribute("quizTaskResult", quizTaskResult.getId());
//            return "redirect:/result/quiz/{userId}/{quizResultId}/{quizTaskResult}";
//        }
//    }

}
