package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.result.QuizResult;
import org.springframework.data.repository.CrudRepository;

public interface QuizResultRepository extends CrudRepository<QuizResult, Long> {
    void deleteQuizResultsByQuiz(Quiz quiz);
}
