package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.domain.quiz.QuizTask;
import com.example.servingwebcontent.domain.quiz.result.QuizTaskResult;
import org.springframework.data.repository.CrudRepository;

public interface QuizTaskResultRepository extends CrudRepository<QuizTaskResult, Long> {

    void removeAllByTask(QuizTask task);

}
