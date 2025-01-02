package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTopicResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalTopicResultRepository extends JpaRepository<MedicalTopicResultEntity, Long> {

	void deleteAllByTopicId(Long id);

	List<MedicalTopicResultEntity> findAllByUserId(Long userId);
}
