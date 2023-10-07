package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.QuizResultRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizInvokeService {

    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;

    public QuizInvokeService(UserRepository userRepository, QuizResultRepository quizResultRepository) {
        this.userRepository = userRepository;
        this.quizResultRepository = quizResultRepository;
    }

    public List<QuizResultBean> getQuizResults(long userId) {
        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
        final List<QuizResultBean> quizzes = new ArrayList<>();
        for (QuizResult result : user.getResults()) {
            final boolean inProgress = result.getTaskList().stream().anyMatch(QuizTaskResult::isComplete);
            quizzes.add(new QuizResultBean(result.getQuiz().getName(), inProgress, result.isComplete(), result.getId(), getProgress(result), result.getCompleteDate()));
        }
        quizzes.sort(Comparator.comparing(QuizResultBean::completeDate, Comparator.nullsFirst(Comparator.reverseOrder())));
        return quizzes;
    }


    public String getProgress(QuizResult quizResult) {
        final long countCompleted = quizResult.getTaskList().stream().filter(QuizTaskResult::isComplete).count();
        final int taskCount = quizResult.getTaskList().size();
        return countCompleted + "/" + taskCount;
    }

    public void startQuiz(long userId, Quiz quiz) {
        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
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
        quizResult.setUser(user);
        quizResultRepository.save(quizResult);
    }

    public void completeQuiz(QuizResult quizResult) {
        quizResult.setComplete(true);
        quizResult.setCompleteDate(new Date());
        quizResultRepository.save(quizResult);
    }

    public void saveTask(QuizTaskResult task) {
        quizResultRepository.save(task.getQuiz());
    }

    public boolean userNotContainsQuiz(Long userId, Long quizResultId) {
        final User user = userRepository.findById(userId).orElseThrow(); // нужно для актуализации данных из бд
        return user.getResults().stream().noneMatch(r -> r.getId().equals(quizResultId));
    }


    public record QuizResultBean(String name, boolean inProgress, boolean complete, Long quizId, String progress, Date completeDate) {}

}
