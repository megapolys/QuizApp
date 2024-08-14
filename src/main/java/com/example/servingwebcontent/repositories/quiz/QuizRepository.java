package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<QuizEntity, Long> {

	QuizEntity findByName(String name);
	QuizEntity findByShortName(String quizShortName);

	List<QuizEntity> findAllByOrderByShortName();
}
