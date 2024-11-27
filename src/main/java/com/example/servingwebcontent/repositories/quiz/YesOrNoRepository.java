package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YesOrNoRepository extends JpaRepository<YesOrNoTaskEntity, Long> {
}
