package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizResultEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuizResultRepository extends CrudRepository<QuizResultEntity, Long> {

	void deleteAllByQuizId(Long id);
}
