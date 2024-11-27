package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskDecisionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizTaskDecisionsRepository extends JpaRepository<QuizTaskDecisionsEntity, QuizTaskDecisionsEntity> {

	void deleteAllByQuizTaskId(Long quizTaskId);
}
