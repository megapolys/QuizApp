package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizResultRepository extends CrudRepository<QuizResult, Long> {
    void deleteQuizResultsByQuiz(Quiz quiz);
}
