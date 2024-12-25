package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {

	void deleteAllByQuizId(Long id);

	List<QuizResultEntity> findAllByUserId(Long userId);
}
