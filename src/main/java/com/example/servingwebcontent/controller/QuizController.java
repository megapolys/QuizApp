package com.example.servingwebcontent.controller;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.repositories.*;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quiz")
@PreAuthorize("hasAuthority('ADMIN')")
public class QuizController {

    private final QuizRepository quizRepository;
    private final QuizTaskRepository quizTaskRepository;
    private final QuizDecisionRepository quizDecisionRepository;
    private final QuizResultRepository quizResultRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public QuizController(QuizRepository quizRepository, QuizTaskRepository quizTaskRepository, QuizDecisionRepository quizDecisionRepository, QuizResultRepository quizResultRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.quizTaskRepository = quizTaskRepository;
        this.quizDecisionRepository = quizDecisionRepository;
        this.quizResultRepository = quizResultRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public String getQuizList(Model model) {
        model.addAttribute("quizList", quizRepository.findAll());
        return "quizList";
    }

    @GetMapping("/add")
    public String addQuiz(
            @RequestParam String quizShortName,
            @RequestParam String quizName,
            RedirectAttributes redirectAttributes
    ) {
        if (quizRepository.findByName(quizName) != null) {
            redirectAttributes.addFlashAttribute("message", "Тест с таким именем уже существует! Придумывай новое...");
        } else if (!StringUtils.hasText(quizName)) {
            redirectAttributes.addFlashAttribute("message", "Наименование теста не может быть пустым!");
        } else if (quizRepository.findByShortName(quizShortName) != null) {
            redirectAttributes.addFlashAttribute("message", "Тест с таким индексом уже существует! Придумывай новое...");
        } else if (!StringUtils.hasText(quizShortName)) {
            redirectAttributes.addFlashAttribute("message", "Индекс не может быть пустым!");
        } else if (quizShortName.length() > 5) {
            redirectAttributes.addFlashAttribute("message", "Индекс должен быть не длиннее 5 символов");
        } else {
            Quiz quiz = new Quiz();
            quiz.setShortName(quizShortName);
            quiz.setName(quizName);
            quizRepository.save(quiz);
        }

        return "redirect:/quiz/list";
    }

    @Transactional
    @GetMapping("/delete/{quiz}")
    public String deleteQuiz(@PathVariable Quiz quiz) {
        for (User user : userRepository.findAll()) {
            user.getQuizzes().remove(quiz);
            user.getResults().removeIf(result -> Objects.equals(result.getQuiz().getId(), quiz.getId()));
            userRepository.save(user);
        }
        quizResultRepository.deleteQuizResultsByQuiz(quiz);
        quizRepository.delete(quiz);
        return "redirect:/quiz/list";
    }

    @GetMapping("/rename/{quiz}")
    public String renameQuiz(@RequestParam String quizName, @PathVariable Quiz quiz) {
        quiz.setName(quizName);
        quizRepository.save(quiz);
        return "redirect:/quiz/{quiz}";
    }

    @GetMapping("{quiz}")
    public String quiz(@PathVariable Quiz quiz, Model model) {
        model.addAttribute("quiz", quiz);
        model.addAttribute("taskList", quiz.getTaskList().stream().sorted(Comparator.comparingInt(QuizTask::getPosition)).toList());
        model.addAttribute("decisions", quizDecisionRepository.findAll());
        return "quiz";
    }

    @PostMapping("/task/add/{type}")
    public String addQuizTask(
            @RequestParam Quiz quiz,
            @RequestParam String position,
            @RequestParam String questionText,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String firstWeight,
            @RequestParam(required = false) String secondWeight,
            @RequestParam(required = false) String thirdWeight,
            @RequestParam(required = false) String fourthWeight,
            @RequestParam(required = false) String fifthWeight,
            @RequestParam(required = false) String yesWeight,
            @RequestParam(required = false) String noWeight,
            @RequestParam(name = "decisions", required = false) QuizDecision[] decisions,
            RedirectAttributes redirectAttributes,
            @PathVariable String type
    ) throws IOException {
        final QuizTask quizTask = new QuizTask();
        String resultFileName = null;
        if (!file.isEmpty()) {
            final File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            final String uuid = UUID.randomUUID().toString();
            resultFileName = uuid + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + "/" + resultFileName));
        }
        if (type.equals("fiveVariant")) {
            final FiveVariantTask fiveVariantTask = new FiveVariantTask();
            fiveVariantTask.setQuestionText(questionText);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                fiveVariantTask.setFileName(resultFileName);
            }
            fiveVariantTask.setFirstWeight(nullOrFloat(firstWeight));
            fiveVariantTask.setSecondWeight(nullOrFloat(secondWeight));
            fiveVariantTask.setThirdWeight(nullOrFloat(thirdWeight));
            fiveVariantTask.setFourthWeight(nullOrFloat(fourthWeight));
            fiveVariantTask.setFifthWeight(nullOrFloat(fifthWeight));
            quizTask.setFiveVariantTask(fiveVariantTask);
        } else if (type.equals("yesOrNo")) {
            final YesOrNoTask yesOrNoTask = new YesOrNoTask();
            yesOrNoTask.setQuestionText(questionText);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                yesOrNoTask.setFileName(resultFileName);
            }
            yesOrNoTask.setYesWeight(nullOrFloat(yesWeight));
            yesOrNoTask.setYesWeight(nullOrFloat(noWeight));
            quizTask.setYesOrNoTask(yesOrNoTask);
        }
        final int pos = Integer.parseInt(position);
        if (quizTaskRepository.findByPositionAndQuiz(pos, quiz) != null) {
            for (QuizTask task : quiz.getTaskList()) {
                if (task.getPosition() >= pos) {
                    task.setPosition(task.getPosition() + 1);
                    quizTaskRepository.save(task);
                }
            }
        }
        quizTask.setPosition(pos);
        quizTask.setQuiz(quiz);
        quizTask.setDecisions(Arrays.stream(decisions).collect(Collectors.toSet()));
        quiz.getTaskList().add(quizTask);
        quizRepository.save(quiz);
        redirectAttributes.addAttribute("quizId", quiz.getId());
        return "redirect:/quiz/{quizId}";
    }

    private Float nullOrFloat(String num) {
        if (StringUtils.hasText(num)) {
            return Float.parseFloat(num);
        }
        return null;
    }

    @GetMapping("/task/delete/{task}")
    public String deleteQuizTask(@PathVariable QuizTask task) {
        quizTaskRepository.delete(task);
        final String fileName = task.getFiveVariantTask() != null ? task.getFiveVariantTask().getFileName()
                : task.getYesOrNoTask() != null ? task.getYesOrNoTask().getFileName() : null;
        new File(uploadPath + "/" + fileName).delete();
        final Quiz quiz = task.getQuiz();
        for (QuizTask quizTask : quiz.getTaskList()) {
            if (quizTask.getPosition() > task.getPosition()) {
                quizTask.setPosition(quizTask.getPosition() - 1);
                quizTaskRepository.save(quizTask);
            }
        }
        return "redirect:/quiz/" + quiz.getId();
    }


}
