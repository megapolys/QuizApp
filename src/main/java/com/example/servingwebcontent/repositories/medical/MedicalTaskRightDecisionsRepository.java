package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.decision.MedicalTaskRightDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalTaskRightDecisionsRepository extends JpaRepository<MedicalTaskRightDecisionEntity, MedicalTaskRightDecisionEntity> {
	List<MedicalTaskRightDecisionEntity> findAllByMedicalTaskId(Long id);

	void deleteAllByMedicalTaskId(Long taskId);
}
