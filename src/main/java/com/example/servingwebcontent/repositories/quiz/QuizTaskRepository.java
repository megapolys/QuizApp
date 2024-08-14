package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuizTaskRepository extends CrudRepository<QuizTaskEntity, Long> {
//    QuizTaskEntity findByPositionAndQuiz(int pos, Quiz quiz);
}
