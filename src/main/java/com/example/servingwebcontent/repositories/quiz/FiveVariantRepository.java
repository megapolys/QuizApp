package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FiveVariantRepository extends JpaRepository<FiveVariantTaskEntity, Long> {
}
