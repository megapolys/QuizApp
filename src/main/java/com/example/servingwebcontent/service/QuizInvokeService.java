package com.example.servingwebcontent.service;

import com.example.servingwebcontent.domain.User;
import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import com.example.servingwebcontent.repositories.QuizResultRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizInvokeService {

    private final UserRepository userRepository;
    public final QuizResultRepository quizResultRepository;

    public QuizInvokeService(UserRepository userRepository, QuizResultRepository quizResultRepository) {
        this.userRepository = userRepository;
        this.quizResultRepository = quizResultRepository;
    }

    public List<QuizBean> getQuizzes(long userId) {
        final User user = userRepository.findById(userId).get();
        final Set<QuizResult> results = user.getResults();
        final List<QuizBean> quizzes = new ArrayList<>();
        for (Quiz quiz : user.getQuizzes()) {
            final Optional<QuizResult> quizResultOptional = results.stream().filter(r -> Objects.equals(r.getQuiz().getId(), quiz.getId())).findFirst();
            if (quizResultOptional.isPresent()) {
                final QuizResult quizResult = quizResultOptional.get();
                quizzes.add(new QuizBean(quiz.getName(), true, quizResult.isComplete(), quizResult.getId(), getProgress(quizResult)));
            } else {
                quizzes.add(new QuizBean(quiz.getName(), false, false, quiz.getId(), null));
            }
        }
        return quizzes;
    }

    public String getProgress(QuizResult quizResult) {
        final long countCompleted = quizResult.getTaskList().stream().filter(QuizTaskResult::isComplete).count();
        final int taskCount = quizResult.getTaskList().size();
        return countCompleted + "/" + taskCount;
    }

    public QuizResult startQuiz(long userId, Quiz quiz) {
        final User user = userRepository.findById(userId).get();
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
        return savedQuizResult;
    }

    public void completeQuiz(QuizResult quizResult) {
        quizResult.setComplete(true);
        quizResult.setCompleteDate(new Date());
        quizResultRepository.save(quizResult);
    }

    public void saveTask(QuizTaskResult task) {
        quizResultRepository.save(task.getQuiz());
    }

    public boolean isUserContainsQuiz(Long userId, Long quizResultId) {
        final User user = userRepository.findById(userId).get();
        return user.getResults().stream().anyMatch(r -> r.getId().equals(quizResultId));
    }


    public record QuizBean(String name, boolean inProgress, boolean complete, Long quizId, String progress) {
    }

}
