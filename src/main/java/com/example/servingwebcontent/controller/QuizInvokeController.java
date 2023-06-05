package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.repositories.QuizResultRepository;
import com.example.servingwebcontent.repositories.QuizTaskResultRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import freemarker.template.utility.StringUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class QuizInvokeController {

    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizTaskResultRepository quizTaskResultRepository;

    public QuizInvokeController(UserRepository userRepository, QuizResultRepository quizResultRepository, QuizTaskResultRepository quizTaskResultRepository) {
        this.userRepository = userRepository;
        this.quizResultRepository = quizResultRepository;
        this.quizTaskResultRepository = quizTaskResultRepository;
    }

    @GetMapping("/quizMain")
    public String quizMain(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        user = userRepository.findById(user.getId()).get();
        final Set<QuizResult> results = user.getResults();
        final Set<QuizBean> quizzes = new LinkedHashSet<>();
        for (Quiz quiz : user.getQuizzes()) {
            final Optional<QuizResult> quizResultOptional = results.stream().filter(r -> Objects.equals(r.getQuiz().getId(), quiz.getId())).findFirst();
            if (quizResultOptional.isPresent()) {
                quizzes.add(new QuizBean(quiz.getName(), true, quizResultOptional.get().isComplete(), quizResultOptional.get().getId()));
            } else {
                quizzes.add(new QuizBean(quiz.getName(), false, false, quiz.getId()));
            }
        }
        model.addAttribute("quizzes", quizzes);
        return "quizMain";
    }

    public record QuizBean(String name, boolean inProgress, boolean complete, Long quizId) {
    }

    @GetMapping("/startQuiz/{quiz}")
    public String startQuiz(
            @AuthenticationPrincipal User user,
            @PathVariable Quiz quiz,
            RedirectAttributes redirectAttributes
    ) {
        user = userRepository.findById(user.getId()).get();
        final Set<QuizResult> results = user.getResults();
        final QuizResult quizResult = new QuizResult();
        quizResult.setQuiz(quiz);
        quizResult.setComplete(false);
        final Set<QuizTaskResult> quizTaskResults = quiz.getTaskList().stream().map(task -> {
            final QuizTaskResult quizTaskResult = new QuizTaskResult();
            quizTaskResult.setTask(task);
            quizTaskResult.setComplete(false);
            quizTaskResult.setQuiz(quizResult);
            return quizTaskResult;
        }).collect(Collectors.toSet());
        quizResult.setTaskList(quizTaskResults);
        final QuizResult savedQuizResult = quizResultRepository.save(quizResult);
        results.add(quizResult);
        user.setResults(results);
        userRepository.save(user);
        redirectAttributes.addAttribute("id", savedQuizResult.getId());
        return "redirect:/invokeQuiz/{id}";
    }

    @GetMapping("/invokeQuiz/{quizResult}")
    public String invokeQuiz(
            @PathVariable QuizResult quizResult,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String areaText,
            Model model
    ) {
        final long countCompleted = quizResult.getTaskList().stream().filter(QuizTaskResult::isComplete).count();
        final Optional<QuizTaskResult> optionalTask = quizResult.getTaskList().stream()
                .filter(task -> !task.isComplete())
                .min(Comparator.comparingInt(task -> task.getTask().getPosition()));
        final boolean complete = optionalTask.isEmpty();
        if (complete) {
            quizResult.setComplete(true);
            quizResultRepository.save(quizResult);
            return "redirect:/quizMain";
        }
        model.addAttribute("message", message);
        model.addAttribute("areaText", areaText);
        model.addAttribute("task", optionalTask.orElse(null));
        final int taskCount = quizResult.getTaskList().size();
        model.addAttribute("lastTask", countCompleted == taskCount - 1);
        model.addAttribute("pagination", countCompleted + "/" + taskCount);
        model.addAttribute("progress", countCompleted * 100f / taskCount);
        return "invokeQuiz";
    }

    @PostMapping("/invokeQuiz/{quizResult}")
    public String saveTaskResult(
            @PathVariable Long quizResult,
            @RequestParam(required = false) String variant,
            @RequestParam String text,
            @RequestParam QuizTaskResult task,
            RedirectAttributes redirectAttributes
    ) {
        if (StringUtils.hasText(variant)) {
            task.setVariant(variant);
            task.setText(text);
            task.setComplete(true);
            quizResultRepository.save(task.getQuiz());
        } else {
            redirectAttributes.addAttribute("message", "Необходимо выбрать вариант ответа");
            redirectAttributes.addAttribute("areaText", text);
        }
        redirectAttributes.addAttribute("quizResult", quizResult);
        return "redirect:/invokeQuiz/{quizResult}";
    }

}
