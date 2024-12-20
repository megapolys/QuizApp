package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.model.quiz.result.QuizResult;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.service.quiz.QuizInvokeService;
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
        model.addAttribute("quizzes", quizInvokeService.getQuizResults(user.getId()));
        model.addAttribute("invokeQuizTab", "active");
        return "invoke/quizList";
    }

    @GetMapping("/invokeQuiz/{quizResult}")
    public String invokeQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable QuizResult quizResult,
            Model model
    ) {
        if (quizInvokeService.userNotContainsQuiz(user.getId(), quizResult.getId())) {
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
        model.addAttribute("invokeQuizTab", "active");
        return "invoke/quiz";
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
        if (quizInvokeService.userNotContainsQuiz(user.getId(), quizResult)) {
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
