package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends CrudRepository<QuizEntity, Long> {

	Optional<QuizEntity> findByShortName(String quizShortName);

	List<QuizEntity> findAllByOrderByShortName();
}
