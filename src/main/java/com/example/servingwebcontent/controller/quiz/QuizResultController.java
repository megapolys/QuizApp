package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
import com.example.servingwebcontent.service.quiz.QuizResultService;
import com.example.servingwebcontent.service.quiz.QuizService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/result/quiz")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizResultController {

    private final QuizResultService quizResultService;
    private final QuizInvokeService quizInvokeService;
    private final QuizService quizService;

    public QuizResultController(QuizResultService quizResultService, QuizInvokeService quizInvokeService, QuizService quizService) {
        this.quizResultService = quizResultService;
        this.quizInvokeService = quizInvokeService;
        this.quizService = quizService;
    }

    @GetMapping("/{user}")
    public String getQuizzes(
            @PathVariable User user,
            Model model
    ) {
        model.addAttribute("quizResults", quizResultService.getResults(user));
        model.addAttribute("quizList", quizService.getQuizzes(user));
        model.addAttribute("user", user);
        model.addAttribute("usersTab", "active");
        return "result/quizList";
    }

    @GetMapping("/{userId}/newQuiz/{quiz}")
    public String newQuiz(
            @PathVariable Long userId,
            @PathVariable Quiz quiz,
            RedirectAttributes redirectAttributes
    ) {
        quizInvokeService.startQuiz(userId, quiz);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/result/quiz/{userId}";
    }

    @GetMapping("/{userId}/delete/{quizResultId}")
    public String deleteQuizResult(
            @PathVariable Long userId,
            @PathVariable Long quizResultId,
            RedirectAttributes redirectAttributes
    ) {
        quizResultService.deleteResult(userId, quizResultId);
        redirectAttributes.addAttribute("userId", userId);
        return "redirect:/result/quiz/{userId}";
    }

    @GetMapping("/{userId}/{quizResult}")
    public String getQuizResult(
            @PathVariable Long userId,
            @PathVariable QuizResult quizResult,
            Model model
    ) {
        model.addAttribute("result", quizResultService.getResult(quizResult));
        model.addAttribute("userId", userId);
        model.addAttribute("usersTab", "active");
        return "result/quizResult";
    }

    @GetMapping("/{userId}/{quizResultId}/{quizTaskResult}")
    public String getQuizResult(
            @PathVariable Long userId,
            @PathVariable Long quizResultId,
            @PathVariable QuizTaskResult quizTaskResult,
            Model model
    ) {
        model.addAttribute("task", quizTaskResult);
        model.addAttribute("score", quizResultService.getWeight(quizTaskResult));
        model.addAttribute("userId", userId);
        model.addAttribute("quizResultId", quizResultId);
        model.addAttribute("usersTab", "active");
        return "result/quizTask";
    }

    @PostMapping("/{userId}/{quizResultId}/{quizTaskResult}")
    public String saveTask(
            @PathVariable Long userId,
            @PathVariable Long quizResultId,
            @PathVariable QuizTaskResult quizTaskResult,
            @RequestParam Float altScore,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addAttribute("userId", userId);
        redirectAttributes.addAttribute("quizResultId", quizResultId);
        if (altScore != null) {
            quizTaskResult.setAltScore(altScore);
            quizInvokeService.saveTask(quizTaskResult);
            redirectAttributes.addFlashAttribute("successMessage", "Изменение сохранено");
            return "redirect:/result/quiz/{userId}/{quizResultId}";
        } else {
            redirectAttributes.addFlashAttribute("message", "Введите балл");
            redirectAttributes.addAttribute("quizTaskResult", quizTaskResult.getId());
            return "redirect:/result/quiz/{userId}/{quizResultId}/{quizTaskResult}";
        }
    }

}
