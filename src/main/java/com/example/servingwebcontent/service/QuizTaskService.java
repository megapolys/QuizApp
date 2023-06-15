package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.task.FiveVariantTask;
import com.example.servingwebcontent.domain.quiz.task.YesOrNoTask;
import com.example.servingwebcontent.repositories.QuizRepository;
import com.example.servingwebcontent.repositories.QuizResultRepository;
import com.example.servingwebcontent.repositories.QuizTaskRepository;
import com.example.servingwebcontent.repositories.QuizTaskResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class QuizTaskService {

    @Value("${upload.path.img}")
    private String uploadPath;

    @Value("${upload.path.imgPrefix}")
    private String uploadPathPrefix;

    private final QuizRepository quizRepository;
    private final QuizTaskRepository quizTaskRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizTaskResultRepository quizTaskResultRepository;

    public QuizTaskService(QuizRepository quizRepository, QuizTaskRepository quizTaskRepository, QuizResultRepository quizResultRepository, QuizTaskResultRepository quizTaskResultRepository) {
        this.quizRepository = quizRepository;
        this.quizTaskRepository = quizTaskRepository;
        this.quizResultRepository = quizResultRepository;
        this.quizTaskResultRepository = quizTaskResultRepository;
    }

    public QuizTaskResult saveFiveVariant(Quiz quiz, FiveVariantTask task, MultipartFile file, int pos, @NonNull Set<QuizDecision> decisions) {
        return saveFiveVariant(quiz, task, file, pos, decisions, new QuizTask());
    }

    public QuizTaskResult saveFiveVariant(Quiz quiz, FiveVariantTask task, MultipartFile file, int pos, Set<QuizDecision> decisions, QuizTask quizTask) {
        try {
            deleteFile(quizTask);
            final String resultFileName = saveFile(file);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                task.setFileName(resultFileName);
            }
        } catch (IOException e) {
            return new QuizTaskResult(ResultType.FILE_EXCEPTION, null);
        }
        quizTask.setFiveVariantTask(task);
        return finalSaving(quiz, pos, decisions, quizTask);
    }

    public QuizTaskResult saveYesOrNo(Quiz quiz, YesOrNoTask task, MultipartFile file, int pos, @NonNull Set<QuizDecision> decisions) {
        return saveYesOrNo(quiz, task, file, pos, decisions, new QuizTask());
    }

    public QuizTaskResult saveYesOrNo(Quiz quiz, YesOrNoTask task, MultipartFile file, int pos, Set<QuizDecision> decisions, QuizTask quizTask) {
        try {
            deleteFile(quizTask);
            final String resultFileName = saveFile(file);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                task.setFileName(resultFileName);
            }
        } catch (IOException e) {
            return new QuizTaskResult(ResultType.FILE_EXCEPTION, null);
        }
        quizTask.setYesOrNoTask(task);
        return finalSaving(quiz, pos, decisions, quizTask);
    }

    private QuizTaskResult finalSaving(Quiz quiz, int pos, Set<QuizDecision> decisions, QuizTask quizTask) {
        movePosition(quiz, pos, quizTask);
        quizTask.setPosition(pos);
        quizTask.setQuiz(quiz);
        quizTask.setDecisions(decisions);
        quizTask = quizTaskRepository.save(quizTask);
        quiz.getTaskList().add(quizTask);
        quizRepository.save(quiz);
        return new QuizTaskResult(ResultType.SUCCESS, quizTask);
    }

    private String saveFile(MultipartFile file) throws IOException {
        String resultFileName = null;
        if (!file.isEmpty()) {
            final File uploadDir = new File(uploadPathPrefix + uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            final String uuid = UUID.randomUUID().toString();
            resultFileName = uuid + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadDir + "/" + resultFileName));
        }
        return resultFileName;
    }

    private void movePosition(Quiz quiz, int pos, QuizTask quizTask) {
        final QuizTask foundTask = quizTaskRepository.findByPositionAndQuiz(pos, quiz);
        if (foundTask != null && !Objects.equals(foundTask.getId(), quizTask.getId())) {
            for (QuizTask task : quiz.getTaskList()) {
                if (task.getPosition() >= pos) {
                    task.setPosition(task.getPosition() + 1);
                    quizTaskRepository.save(task);
                }
            }
        }
    }

    @Transactional
    public void delete(QuizTask task) {
        for (QuizResult quizResult : quizResultRepository.findAll()) {
            final boolean removed = quizResult.getTaskList()
                    .removeIf(resultTask -> Objects.equals(resultTask.getTask().getId(), task.getId()));
            if (removed) {
                quizResultRepository.save(quizResult);
            }
        }
        quizTaskResultRepository.removeAllByTask(task);
        deleteFile(task);
        final Quiz quiz = task.getQuiz();
        for (QuizTask quizTask : quiz.getTaskList()) {
            if (quizTask.getPosition() > task.getPosition()) {
                quizTask.setPosition(quizTask.getPosition() - 1);
                quizTaskRepository.save(quizTask);
            }
        }
        quiz.getTaskList().remove(task);
        quizRepository.save(quiz);
        quizTaskRepository.delete(task);
    }

    private void deleteFile(QuizTask task) {
        final String fileName = task.getFiveVariantTask() != null ? task.getFiveVariantTask().getFileName()
                : task.getYesOrNoTask() != null ? task.getYesOrNoTask().getFileName() : null;
        new File(uploadPathPrefix + uploadPath + "/" + fileName).delete();
    }

    public enum ResultType {
        FILE_EXCEPTION, SUCCESS
    }

    public record QuizTaskResult(ResultType result, QuizTask task) {
    }

}
