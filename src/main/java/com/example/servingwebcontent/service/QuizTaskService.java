package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizDecision;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.TaskType;
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

    public QuizTaskResult save(Quiz quiz, Object task, MultipartFile file, int pos, @NonNull Set<QuizDecision> decisions, TaskType type) {
        QuizTask quizTask = new QuizTask();
        try {
            final String resultFileName = saveFile(file);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                if (type == TaskType.FIVE_VARIANT) {
                    ((FiveVariantTask)task).setFileName(resultFileName);
                }
                if (type == TaskType.YES_OR_NO) {
                    ((YesOrNoTask)task).setFileName(resultFileName);
                }
            }
        } catch (IOException e) {
            return new QuizTaskResult(ResultType.FILE_EXCEPTION, null);
        }
        if (type == TaskType.FIVE_VARIANT) {
            quizTask.setFiveVariantTask((FiveVariantTask)task);
        }
        if (type == TaskType.YES_OR_NO) {
            quizTask.setYesOrNoTask((YesOrNoTask)task);
        }
        movePosition(quiz, pos);
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

    private void movePosition(Quiz quiz, int pos) {
        if (quizTaskRepository.findByPositionAndQuiz(pos, quiz) != null) {
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
        final String fileName = task.getFiveVariantTask() != null ? task.getFiveVariantTask().getFileName()
                : task.getYesOrNoTask() != null ? task.getYesOrNoTask().getFileName() : null;
        new File(uploadPathPrefix + uploadPath + "/" + fileName).delete();
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

    public enum ResultType {
        FILE_EXCEPTION, SUCCESS
    }

    public record QuizTaskResult(ResultType result, QuizTask task) {
    }

}
