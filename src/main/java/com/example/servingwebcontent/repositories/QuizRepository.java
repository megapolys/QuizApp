package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.Quiz;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuizRepository extends CrudRepository<Quiz, Long> {

    Quiz findByName(String name);
    Quiz findByShortName(String quizShortName);

    List<Quiz> findAllByOrderByShortName();
}
