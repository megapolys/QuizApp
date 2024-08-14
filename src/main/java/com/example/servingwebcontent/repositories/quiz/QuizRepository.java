package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<Quiz, Long> {

    Quiz findByName(String name);
    Quiz findByShortName(String quizShortName);

    List<Quiz> findAllByOrderByShortName();
}
