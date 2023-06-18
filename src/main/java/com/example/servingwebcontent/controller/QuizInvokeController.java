package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.service.QuizInvokeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.Optional;

@Controller
public class QuizInvokeController {

    private final QuizInvokeService quizInvokeService;

    public QuizInvokeController(QuizInvokeService quizInvokeService) {
        this.quizInvokeService = quizInvokeService;
    }

    @GetMapping("/userQuizList")
    public String quizMain(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("quizzes", quizInvokeService.getQuizzes(user.getId()));
        return "userQuizList";
    }

    @GetMapping("/startQuiz/{quiz}")
    public String startQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable Quiz quiz,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addAttribute("quizResult", quizInvokeService.startQuiz(user.getId(), quiz));
        return "redirect:/invokeQuiz/{quizResult}";
    }

    @GetMapping("/invokeQuiz/{quizResult}")
    public String invokeQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable QuizResult quizResult,
            Model model
    ) {
        if (!quizInvokeService.isUserContainsQuiz(user.getId(), quizResult.getId())) {
            throw new RuntimeException("Access denied!");
        }
        final long countCompleted = quizResult.getTaskList().stream().filter(QuizTaskResult::isComplete).count();
        final Optional<QuizTaskResult> optionalTask = quizResult.getTaskList().stream()
                .filter(task -> !task.isComplete())
                .min(Comparator.comparingInt(task -> task.getTask().getPosition()));
        final boolean complete = optionalTask.isEmpty();
        if (complete) {
            quizInvokeService.completeQuiz(quizResult);
            return "redirect:/userQuizList";
        }
        model.addAttribute("task", optionalTask.orElse(null));
        final int taskCount = quizResult.getTaskList().size();
        model.addAttribute("lastTask", countCompleted == taskCount - 1);
        model.addAttribute("pagination", countCompleted + "/" + taskCount);
        model.addAttribute("progress", countCompleted * 100f / taskCount);
        return "invokeQuiz";
    }

    @PostMapping("/invokeQuiz/{quizResult}")
    public String saveTaskResult(
            @AuthenticationPrincipal User user,
            @PathVariable Long quizResult,
            @RequestParam(required = false) String variant,
            @RequestParam String text,
            @RequestParam QuizTaskResult task,
            RedirectAttributes redirectAttributes
    ) {
        if (!quizInvokeService.isUserContainsQuiz(user.getId(), quizResult)) {
            throw new RuntimeException("Access denied!");
        }
        if (StringUtils.hasText(variant)) {
            task.setVariant(variant);
            task.setText(text);
            task.setComplete(true);
            quizInvokeService.saveTask(task);
        } else {
            redirectAttributes.addFlashAttribute("message", "Необходимо выбрать вариант ответа");
            redirectAttributes.addFlashAttribute("areaText", text);
        }
        redirectAttributes.addAttribute("quizResult", quizResult);
        return "redirect:/invokeQuiz/{quizResult}";
    }

}
