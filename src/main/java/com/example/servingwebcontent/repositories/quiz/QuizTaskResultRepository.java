package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuizTaskResultRepository extends CrudRepository<QuizTaskResultEntity, Long> {

//    void removeAllByTask(QuizTask task);

}
