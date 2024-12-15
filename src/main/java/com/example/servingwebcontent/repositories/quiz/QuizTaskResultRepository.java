package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizTaskResultRepository extends JpaRepository<QuizTaskResultEntity, Long> {

	void deleteByTaskId(Long taskId);

}
