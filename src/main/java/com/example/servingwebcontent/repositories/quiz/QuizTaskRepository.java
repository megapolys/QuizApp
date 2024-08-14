package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuizTaskRepository extends CrudRepository<QuizTask, Long> {
    QuizTask findByPositionAndQuiz(int pos, Quiz quiz);
}
