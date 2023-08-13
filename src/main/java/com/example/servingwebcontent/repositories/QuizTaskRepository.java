package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.Quiz;
import com.example.servingwebcontent.domain.quiz.QuizTask;
import org.springframework.data.repository.CrudRepository;

public interface QuizTaskRepository extends CrudRepository<QuizTask, Long> {
    QuizTask findByPositionAndQuiz(int pos, Quiz quiz);
}
