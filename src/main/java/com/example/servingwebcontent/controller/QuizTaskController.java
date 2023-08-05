package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.repositories.QuizDecisionRepository;
import com.example.servingwebcontent.service.QuizTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private final QuizDecisionRepository quizDecisionRepository;

    public QuizTaskController(QuizTaskService quizTaskService, QuizDecisionRepository quizDecisionRepository) {
        this.quizTaskService = quizTaskService;
        this.quizDecisionRepository = quizDecisionRepository;
    }


    @PostMapping("/add/yesOrNo")
    public String addQuizTask(
            @RequestParam Quiz quiz,
            @RequestParam Integer position,
            @RequestParam String preQuestionText,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Float yesWeight,
            @RequestParam(required = false) Float noWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes
    ) {
        YesOrNoTask yesOrNoTask = new YesOrNoTask();
        yesOrNoTask.setPreQuestionText(preQuestionText);
        yesOrNoTask.setQuestionText(questionText);
        yesOrNoTask.setYesWeight(yesWeight);
        yesOrNoTask.setNoWeight(noWeight);
        if (isAttributesValid(redirectAttributes, questionText, position, decisions)){
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveYesOrNo(quiz, yesOrNoTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()));
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                yesOrNoTask = new YesOrNoTask();
                decisions = new QuizDecision[]{};
            }
        }
        redirectAttributes.addFlashAttribute("taskDecisions", decisions);
        redirectAttributes.addFlashAttribute("yesOrNoTask", yesOrNoTask);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    @GetMapping("/update/yesOrNo/{quizTask}")
    public String updateYesOrNo(
            @PathVariable QuizTask quizTask,
            @RequestParam(required = false) YesOrNoTask yesOrNoTask,
            Model model
    ) {
        if (yesOrNoTask == null) {
            yesOrNoTask = quizTask.getYesOrNoTask();
        }
        model.addAttribute("yesOrNoTask", yesOrNoTask);
        model.addAttribute("quizTask", quizTask);
        model.addAttribute("quiz", quizTask.getQuiz());
        model.addAttribute("decisions", quizDecisionRepository.findAllByOrderByName());
        return "yesOrNo";
    }

    @PostMapping("/update/yesOrNo")
    public String updateYesOrNo(
            @RequestParam QuizTask quizTask,
            @RequestParam Integer position,
            @RequestParam String preQuestionText,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Float yesWeight,
            @RequestParam(required = false) Float noWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes
    ) {
        YesOrNoTask yesOrNoTask = new YesOrNoTask();
        yesOrNoTask.setPreQuestionText(preQuestionText);
        yesOrNoTask.setQuestionText(questionText);
        yesOrNoTask.setYesWeight(yesWeight);
        yesOrNoTask.setNoWeight(noWeight);
        if (isAttributesValid(redirectAttributes, questionText, position, decisions)){
            final Quiz quiz = quizTask.getQuiz();
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveYesOrNo(quiz, yesOrNoTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()), quizTask);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                redirectAttributes.addAttribute("quizId", quiz.getId());
                return "redirect:/quiz/{quizId}";
            }
        }
        redirectAttributes.addFlashAttribute("taskDecisions", decisions);
        redirectAttributes.addFlashAttribute("yesOrNoTask", yesOrNoTask);
        redirectAttributes.addFlashAttribute("quizTask", quizTask);
        return "redirect:/quiz/task/update/yesOrNo/{quizTask}";
    }

    @PostMapping("/add/fiveVariant")
    public String addQuizTask(
            @RequestParam Quiz quiz,
            @RequestParam Integer position,
            @RequestParam String preQuestionText,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Float firstWeight,
            @RequestParam(required = false) Float secondWeight,
            @RequestParam(required = false) Float thirdWeight,
            @RequestParam(required = false) Float fourthWeight,
            @RequestParam(required = false) Float fifthWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes
    ) {
        FiveVariantTask fiveVariantTask = getFiveVariantTask(preQuestionText, questionText, firstWeight, secondWeight, thirdWeight, fourthWeight, fifthWeight);
        if (isAttributesValid(redirectAttributes, questionText, position, decisions)){
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveFiveVariant(quiz, fiveVariantTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()));
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                fiveVariantTask = new FiveVariantTask();
                decisions = new QuizDecision[]{};
            }
        }
        redirectAttributes.addFlashAttribute("taskDecisions", decisions);
        redirectAttributes.addFlashAttribute("fiveVariantTask", fiveVariantTask);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    @GetMapping("/update/fiveVariant/{quizTask}")
    public String updateFiveVariant(
            @PathVariable QuizTask quizTask,
            @RequestParam(required = false) FiveVariantTask fiveVariantTask,
            Model model
    ) {
        if (fiveVariantTask == null) {
            fiveVariantTask = quizTask.getFiveVariantTask();
        }
        model.addAttribute("fiveVariantTask", fiveVariantTask);
        model.addAttribute("quizTask", quizTask);
        model.addAttribute("quiz", quizTask.getQuiz());
        model.addAttribute("decisions", quizDecisionRepository.findAllByOrderByName());
        return "fiveVariant";
    }

    @PostMapping("/update/fiveVariant")
    public String updateQuizTask(
            @RequestParam QuizTask quizTask,
            @RequestParam Integer position,
            @RequestParam String preQuestionText,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Float firstWeight,
            @RequestParam(required = false) Float secondWeight,
            @RequestParam(required = false) Float thirdWeight,
            @RequestParam(required = false) Float fourthWeight,
            @RequestParam(required = false) Float fifthWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes
    ) {
        FiveVariantTask fiveVariantTask = getFiveVariantTask(preQuestionText, questionText, firstWeight, secondWeight, thirdWeight, fourthWeight, fifthWeight);
        if (isAttributesValid(redirectAttributes, questionText, position, decisions)){
            final Quiz quiz = quizTask.getQuiz();
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveFiveVariant(quiz, fiveVariantTask, file, position, Arrays.stream(decisions).collect(Collectors.toSet()), quizTask);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                redirectAttributes.addAttribute("quizId", quiz.getId());
                return "redirect:/quiz/{quizId}";
            }
        }
        redirectAttributes.addFlashAttribute("taskDecisions", decisions);
        redirectAttributes.addFlashAttribute("fiveVariantTask", fiveVariantTask);
        redirectAttributes.addFlashAttribute("quizTask", quizTask);
        return "redirect:/quiz/task/update/fiveVariant/{quizTask}";
    }

    private boolean isAttributesValid(RedirectAttributes redirectAttributes, String questionText, Integer position, QuizDecision[] decisions) {
        if (!StringUtils.hasText(questionText)) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести текст вопроса.");
        } else if (position == null) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести номер.");
        } else if (decisions == null || decisions.length == 0) {
            redirectAttributes.addFlashAttribute("message", "Нужно выбрать хотя бы одно решение по вопросу.");
        } else {
            return true;
        }
        return false;
    }

    private static FiveVariantTask getFiveVariantTask(String preQuestionText, String questionText, Float firstWeight, Float secondWeight, Float thirdWeight, Float fourthWeight, Float fifthWeight) {
        FiveVariantTask fiveVariantTask = new FiveVariantTask();

        fiveVariantTask.setPreQuestionText(preQuestionText);
        fiveVariantTask.setQuestionText(questionText);
        fiveVariantTask.setFirstWeight(firstWeight);
        fiveVariantTask.setSecondWeight(secondWeight);
        fiveVariantTask.setThirdWeight(thirdWeight);
        fiveVariantTask.setFourthWeight(fourthWeight);
        fiveVariantTask.setFifthWeight(fifthWeight);
        return fiveVariantTask;
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
