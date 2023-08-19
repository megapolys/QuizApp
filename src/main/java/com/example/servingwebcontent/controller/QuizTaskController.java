package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.decision.QuizDecision;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.domain.validation.TaskForm;
import com.example.servingwebcontent.domain.validation.TaskType;
import com.example.servingwebcontent.service.QuizDecisionService;
import com.example.servingwebcontent.service.QuizTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/quiz/task")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizTaskController {

    private final QuizTaskService quizTaskService;
    private final QuizDecisionService decisionService;

    public QuizTaskController(QuizTaskService quizTaskService,QuizDecisionService decisionService) {
        this.quizTaskService = quizTaskService;
        this.decisionService = decisionService;
    }


    @PostMapping("/add/yesOrNo")
    public String addYesOrNoTask(
            @RequestParam Quiz quiz,
            @RequestParam("file") MultipartFile file,
            TaskForm taskForm,
            RedirectAttributes redirectAttributes
    ) {
        taskForm.setTaskType(TaskType.YES_OR_NO);
        if (isAttributesValid(redirectAttributes, taskForm.getQuestionText(), taskForm.getPosition(), taskForm.getDecisions())){
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveYesOrNo(quiz, file, taskForm);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                taskForm.setQuestionText(null);
                taskForm.setYesWeight(null);
                taskForm.setNoWeight(null);
                taskForm.setDecisions(null);
            }
        }
        redirectAttributes.addFlashAttribute("taskForm", taskForm);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    @GetMapping("/update/yesOrNo/{quizTask}")
    public String updateYesOrNo(
            @PathVariable QuizTask quizTask,
            @RequestParam(required = false) TaskForm taskForm,
            Model model
    ) {
        if (taskForm == null) {
            taskForm = taskFormFromYesOrNoTask(quizTask);
        }

        model.addAttribute("taskForm", taskForm);
        model.addAttribute("quizTask", quizTask);
        model.addAttribute("quiz", quizTask.getQuiz());
        model.addAttribute("groups", decisionService.groups());
        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
        model.addAttribute("path", "/quiz/task/update/yesOrNo");
        model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
        model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
        model.addAttribute("quizTab", "active");
        return "quiz/taskUpdate";
    }

    private TaskForm taskFormFromYesOrNoTask(QuizTask task) {
        final YesOrNoTask yesOrNoTask = task.getYesOrNoTask();
        final TaskForm taskForm = new TaskForm();
        taskForm.setPosition(task.getPosition());
        taskForm.setPreQuestionText(yesOrNoTask.getPreQuestionText());
        taskForm.setQuestionText(yesOrNoTask.getQuestionText());
        taskForm.setTaskType(TaskType.YES_OR_NO);
        taskForm.setYesWeight(yesOrNoTask.getYesWeight());
        taskForm.setNoWeight(yesOrNoTask.getNoWeight());
        taskForm.setDecisions(task.getDecisions());
        taskForm.setFileName(yesOrNoTask.getFileName());
        return taskForm;
    }

    @PostMapping("/update/yesOrNo")
    public String updateYesOrNo(
            @RequestParam QuizTask quizTask,
            @RequestParam("file") MultipartFile file,
            TaskForm taskForm,
            RedirectAttributes redirectAttributes
    ) {
        if (isAttributesValid(redirectAttributes, taskForm.getQuestionText(), taskForm.getPosition(), taskForm.getDecisions())){
            final Quiz quiz = quizTask.getQuiz();
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveYesOrNo(quiz, file, taskForm, quizTask);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                redirectAttributes.addAttribute("quizId", quiz.getId());
                return "redirect:/quiz/{quizId}";
            }
        }
        redirectAttributes.addFlashAttribute("quizTask", quizTask);
        return "redirect:/quiz/task/update/yesOrNo/{quizTask}";
    }

    @PostMapping("/add/fiveVariant")
    public String addFiveVariantTask(
            @RequestParam Quiz quiz,
            @RequestParam("file") MultipartFile file,
            TaskForm taskForm,
            RedirectAttributes redirectAttributes
    ) {
        taskForm.setTaskType(TaskType.FIVE_VARIANT);
        if (isAttributesValid(redirectAttributes, taskForm.getQuestionText(), taskForm.getPosition(), taskForm.getDecisions())){
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveFiveVariant(quiz, file, taskForm);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                taskForm.setQuestionText(null);
                taskForm.setFirstWeight(null);
                taskForm.setSecondWeight(null);
                taskForm.setThirdWeight(null);
                taskForm.setFourthWeight(null);
                taskForm.setFifthWeight(null);
                taskForm.setDecisions(null);
            }
        }
        redirectAttributes.addFlashAttribute("taskForm", taskForm);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    @GetMapping("/update/fiveVariant/{quizTask}")
    public String updateFiveVariant(
            @PathVariable QuizTask quizTask,
            @RequestParam(required = false) TaskForm taskForm,
            Model model
    ) {
        if (taskForm == null) {
            taskForm = taskFormFromFiveVariantTask(quizTask);
        }
        model.addAttribute("taskForm", taskForm);
        model.addAttribute("quizTask", quizTask);
        model.addAttribute("quiz", quizTask.getQuiz());
        model.addAttribute("groups", decisionService.groups());
        model.addAttribute("decisions", decisionService.decisionsWithoutGroups());
        model.addAttribute("path", "/quiz/task/update/fiveVariant");
        model.addAttribute("FIVE_VARIANT", TaskType.FIVE_VARIANT);
        model.addAttribute("YES_OR_NO", TaskType.YES_OR_NO);
        model.addAttribute("quizTab", "active");
        return "quiz/taskUpdate";
    }

    private TaskForm taskFormFromFiveVariantTask(QuizTask task) {
        final FiveVariantTask fiveVariantTask = task.getFiveVariantTask();
        final TaskForm taskForm = new TaskForm();
        taskForm.setPosition(task.getPosition());
        taskForm.setPreQuestionText(fiveVariantTask.getPreQuestionText());
        taskForm.setQuestionText(fiveVariantTask.getQuestionText());
        taskForm.setFirstWeight(fiveVariantTask.getFirstWeight());
        taskForm.setSecondWeight(fiveVariantTask.getSecondWeight());
        taskForm.setThirdWeight(fiveVariantTask.getThirdWeight());
        taskForm.setFourthWeight(fiveVariantTask.getFourthWeight());
        taskForm.setFifthWeight(fiveVariantTask.getFifthWeight());
        taskForm.setDecisions(task.getDecisions());
        taskForm.setTaskType(TaskType.FIVE_VARIANT);
        taskForm.setFileName(fiveVariantTask.getFileName());
        return taskForm;
    }

    @PostMapping("/update/fiveVariant")
    public String updateQuizTask(
            @RequestParam QuizTask quizTask,
            @RequestParam("file") MultipartFile file,
            TaskForm taskForm,
            RedirectAttributes redirectAttributes
    ) {
        if (isAttributesValid(redirectAttributes, taskForm.getQuestionText(), taskForm.getPosition(), taskForm.getDecisions())){
            final Quiz quiz = quizTask.getQuiz();
            final QuizTaskService.QuizTaskResult result = quizTaskService.saveFiveVariant(quiz, file, taskForm, quizTask);
            resultProcess(redirectAttributes, result);
            if (result.result() == QuizTaskService.ResultType.SUCCESS) {
                redirectAttributes.addAttribute("quizId", quiz.getId());
                return "redirect:/quiz/{quizId}";
            }
        }
        redirectAttributes.addFlashAttribute("quizTask", quizTask);
        return "redirect:/quiz/task/update/fiveVariant/{quizTask}";
    }

    private boolean isAttributesValid(RedirectAttributes redirectAttributes, String questionText, Integer position, Set<QuizDecision> decisions) {
        return isAttributesValid(redirectAttributes, questionText, position, decisions == null ? new QuizDecision[]{} : decisions.toArray(new QuizDecision[]{}));
    }

    private boolean isAttributesValid(RedirectAttributes redirectAttributes, String questionText, Integer position, QuizDecision[] decisions) {
        if (!StringUtils.hasText(questionText)) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести текст вопроса.");
        } else if (position == null) {
            redirectAttributes.addFlashAttribute("message", "Необходимо ввести номер.");
        } else {
            return true;
        }
        return false;
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
