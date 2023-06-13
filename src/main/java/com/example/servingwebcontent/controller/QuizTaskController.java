package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.TaskType;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.service.QuizTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz/task")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizTaskController {

    private final QuizTaskService quizTaskService;

    public QuizTaskController(QuizTaskService quizTaskService) {
        this.quizTaskService = quizTaskService;
    }


    @PostMapping("/add/{type}")
    public String addQuizTask(
            @RequestParam Quiz quiz,
            @RequestParam Integer position,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Float firstWeight,
            @RequestParam(required = false) Float secondWeight,
            @RequestParam(required = false) Float thirdWeight,
            @RequestParam(required = false) Float fourthWeight,
            @RequestParam(required = false) Float fifthWeight,
            @RequestParam(required = false) Float yesWeight,
            @RequestParam(required = false) Float noWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes,
            @PathVariable String type
    ) {
        FiveVariantTask fiveVariantTask = new FiveVariantTask();
        fiveVariantTask.setQuestionText(questionText);
        fiveVariantTask.setFirstWeight(firstWeight);
        fiveVariantTask.setSecondWeight(secondWeight);
        fiveVariantTask.setThirdWeight(thirdWeight);
        fiveVariantTask.setFourthWeight(fourthWeight);
        fiveVariantTask.setFifthWeight(fifthWeight);
        YesOrNoTask yesOrNoTask = new YesOrNoTask();
        yesOrNoTask.setQuestionText(questionText);
        yesOrNoTask.setYesWeight(yesWeight);
        yesOrNoTask.setNoWeight(noWeight);
        if (!StringUtils.hasText(questionText)) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести текст вопроса.");
        } else if (position == null) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести номер.");
        } else if (decisions == null || decisions.length == 0) {
            redirectAttributes.addFlashAttribute("message", "Нужно выбрать хотя бы одно решение по вопросу.");
        } else if (type.equals("fiveVariant")) {
            final QuizTaskService.QuizTaskResult result = quizTaskService.save(quiz, fiveVariantTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()), TaskType.FIVE_VARIANT);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                fiveVariantTask = new FiveVariantTask();
                decisions = new QuizDecision[]{};
            }
        } else if (type.equals("yesOrNo")) {
            final QuizTaskService.QuizTaskResult result = quizTaskService.save(quiz, yesOrNoTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()), TaskType.YES_OR_NO);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                yesOrNoTask = new YesOrNoTask();
                decisions = new QuizDecision[]{};
            }
        }
        redirectAttributes.addFlashAttribute("taskDecisions", decisions);
        redirectAttributes.addFlashAttribute("fiveVariantTask", fiveVariantTask);
        redirectAttributes.addFlashAttribute("yesOrNoTask", yesOrNoTask);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    private static void resultProcess(RedirectAttributes redirectAttributes, QuizTaskService.QuizTaskResult result) {
        if (result.result() == QuizTaskService.ResultType.FILE_EXCEPTION) {
            redirectAttributes.addFlashAttribute("message", "Ошибка загрузки файла.");
        }
        if (result.result() == QuizTaskService.ResultType.SUCCESS) {
            redirectAttributes.addFlashAttribute("successMessage", "Вопрос успешно сохранен.");
        }
    }

    @GetMapping("/delete/{task}")
    public String deleteQuizTask(
            @PathVariable QuizTask task,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addAttribute("quizId", task.getQuiz().getId());
        quizTaskService.delete(task);
        return "redirect:/quiz/{quizId}";
    }


}
