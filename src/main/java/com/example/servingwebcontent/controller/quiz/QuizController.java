package com.example.servingwebcontent.controller.quiz;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.validation.TaskForm;
import com.example.servingwebcontent.domain.validation.TaskType;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.service.DecisionService;
import com.example.servingwebcontent.service.quiz.QuizService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;

@Controller
@RequestMapping("/quiz")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuizService quizService;
    private final DecisionService decisionService;
    private final TaskForm taskForm;

    public QuizController(QuizRepository quizRepository, QuizService quizService, DecisionService decisionService, TaskForm taskForm) {
        this.quizRepository = quizRepository;
        this.quizService = quizService;
        this.decisionService = decisionService;
        this.taskForm = taskForm;
    }

    @GetMapping("/list")
    public String getQuizList(Model model) {
        model.addAttribute("quizList", quizRepository.findAllByOrderByShortName());
        model.addAttribute("quizTab", "active");
        return "quiz/quizList";
    }

    @PostMapping("/add")
    public String addQuiz(
            @RequestParam String shortName,
            @RequestParam String name,
            RedirectAttributes redirectAttributes
    ) {
        Quiz quiz = new Quiz();
        addOrUpdateQuiz(shortName, name, redirectAttributes, quiz, false);
        return "redirect:/quiz/list";
    }

    @PostMapping("/rename/{quiz}")
    public String renameQuiz(
            @RequestParam String shortName,
            @RequestParam String name,
            @PathVariable Quiz quiz,
            RedirectAttributes redirectAttributes
    ) {
        addOrUpdateQuiz(shortName, name, redirectAttributes, quiz, true);
        return "redirect:/quiz/{quiz}";
    }

    private void addOrUpdateQuiz(@RequestParam String shortName, @RequestParam String name, RedirectAttributes redirectAttributes, Quiz quiz, boolean update) {
        if (!StringUtils.hasText(name)) {
            redirectAttributes.addFlashAttribute("message", "Наименование теста не может быть пустым!");
        } else if (!StringUtils.hasText(shortName)) {
            redirectAttributes.addFlashAttribute("message", "Индекс не может быть пустым!");
        } else if (shortName.length() > 5) {
            redirectAttributes.addFlashAttribute("message", "Индекс должен быть не длиннее 5 символов");
        } else {
            quiz.setShortName(shortName);
            quiz.setName(name);
            QuizService.QuizResult result = quizService.save(quiz);
            if (result.result() == QuizService.ResultType.NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Тест с таким именем уже существует! Придумывай новое");
            }
            if (result.result() == QuizService.ResultType.SHORT_NAME_FOUND) {
                redirectAttributes.addFlashAttribute("message", "Тест с таким индексом уже существует! Придумывай новый");
            }
            if (result.result() == QuizService.ResultType.SUCCESS) {
                redirectAttributes.addFlashAttribute("successMessage", String.format("Опросник с '%s. %s' успешно %s",
                        result.quiz().getShortName(), result.quiz().getName(), update ? "обновлен" : "добавлен"));
            }
        }
    }

    @PostMapping("/delete/{quiz}")
    public String deleteQuiz(@PathVariable Quiz quiz) {
        quizService.delete(quiz);
        return "redirect:/quiz/list";
    }

    @GetMapping("{quiz}")
    public String quiz(@PathVariable Quiz quiz, Model model) {
        model.addAttribute("quiz", quiz);
        model.addAttribute("taskList", quiz.getTaskList().stream()
                .sorted(Comparator.comparingInt(QuizTask::getPosition)).toList());
        model.addAttribute("groups", decisionService.groups());
        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
        if (model.asMap().get("taskForm") != null) {
            this.taskForm.setFromTaskForm((TaskForm) model.asMap().get("taskForm"));
        }
        this.taskForm.setPosition(quizService.getNextTaskPosition(quiz));
        model.addAttribute("taskForm", this.taskForm);
        model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
        model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
        model.addAttribute("quizTab", "active");
        return "quiz/quiz";
    }

}
