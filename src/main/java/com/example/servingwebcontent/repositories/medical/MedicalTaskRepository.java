package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalTaskRepository extends JpaRepository<MedicalTaskEntity, Long> {

	List<MedicalTaskEntity> findAllByTopicId(Long id);

	Optional<MedicalTaskEntity> findByName(String name);
}
