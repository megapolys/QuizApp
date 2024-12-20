package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalTaskResultRepository extends JpaRepository<MedicalTaskResultEntity, Long> {

	void deleteAllByTaskId(Long taskId);
}
