package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.repositories.QuizRepository;
import com.example.servingwebcontent.repositories.QuizResultRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final QuizTaskService quizTaskService;

    public QuizService(QuizRepository quizRepository, UserRepository userRepository, QuizResultRepository quizResultRepository, QuizTaskService quizTaskService) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
        this.quizResultRepository = quizResultRepository;
        this.quizTaskService = quizTaskService;
    }

    public QuizResult save(Quiz quiz) {
        Quiz byShortName = quizRepository.findByShortName(quiz.getShortName());
        if (byShortName != null && !Objects.equals(byShortName.getId(), quiz.getId())) {
            return new QuizResult(ResultType.SHORT_NAME_FOUND, byShortName);
        }
        final Quiz byName = quizRepository.findByName(quiz.getName());
        if (byName != null && !Objects.equals(byName.getId(), quiz.getId())) {
            return new QuizResult(ResultType.NAME_FOUND, byName);
        }
        return new QuizResult(ResultType.SUCCESS, quizRepository.save(quiz));
    }

    @Transactional
    public void delete(Quiz quiz) {
        for (QuizTask quizTask : quiz.getTaskList()) {
            quizTaskService.delete(quizTask);
        }
        for (User user : userRepository.findAll()) {
            user.getQuizzes().remove(quiz);
            user.getResults().removeIf(result -> Objects.equals(result.getQuiz().getId(), quiz.getId()));
            userRepository.save(user);
        }
        quizResultRepository.deleteQuizResultsByQuiz(quiz);
        quizRepository.delete(quiz);
    }

    public int getNextTaskPosition(Quiz quiz) {
        int max = 1;
        final List<QuizTask> taskList = quiz.getTaskList().stream()
                .sorted(Comparator.comparing(QuizTask::getPosition))
                .toList();
        for (QuizTask task : taskList) {
            if (task.getPosition() == max) {
                max++;
            } else {
                return max;
            }
        }
        return max;
    }

    public enum ResultType {
        SHORT_NAME_FOUND, NAME_FOUND, SUCCESS
    }

    public record QuizResult(ResultType result, Quiz quiz) {
    }

}
