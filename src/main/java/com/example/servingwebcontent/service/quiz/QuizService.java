package com.example.servingwebcontent.service.quiz;

import com.example.servingwebcontent.model.quiz.Quiz;
import com.example.servingwebcontent.model.quiz.QuizTask;
import com.example.servingwebcontent.model.user.User;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.repositories.quiz.QuizRepository;
import com.example.servingwebcontent.repositories.quiz.QuizResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

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
    
    public List<QuizBean> getQuizzes(User user) {
        final Set<com.example.servingwebcontent.domain.quiz.result.QuizResult> results = user.getResults();
        final List<QuizBean> quizList = new ArrayList<>();
        for (Quiz quiz : quizRepository.findAllByOrderByShortName()) {
            boolean inProgress = false;
            boolean exists = false;
            for (com.example.servingwebcontent.domain.quiz.result.QuizResult result : results) {
                if (Objects.equals(quiz.getId(), result.getQuiz().getId())) {
                    exists = true;
                    if (!result.isComplete()) {
                        inProgress = true;
                    }
                }
            }
            quizList.add(new QuizBean(quiz, inProgress, exists));
        }
        return quizList;
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
    
    public record QuizBean(Quiz quiz, boolean inProgress, boolean exists) {
    }

    public enum ResultType {
        SHORT_NAME_FOUND, NAME_FOUND, SUCCESS
    }

    public record QuizResult(ResultType result, Quiz quiz) {
    }

}
