package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.quiz.Quiz;
import org.springframework.data.repository.CrudRepository;

public interface QuizRepository extends CrudRepository<Quiz, Integer> {

    Quiz findByName(String name);
    Quiz findByShortName(String quizShortName);
}
