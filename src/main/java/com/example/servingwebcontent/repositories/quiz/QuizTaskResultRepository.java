package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizTaskResultRepository extends JpaRepository<QuizTaskResultEntity, Long> {

	void deleteByTaskId(Long taskId);

	List<QuizTaskResultEntity> findAllByQuizResultId(Long id);
}
