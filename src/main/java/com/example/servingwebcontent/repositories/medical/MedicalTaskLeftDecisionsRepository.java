package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.decision.MedicalTaskLeftDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalTaskLeftDecisionsRepository extends JpaRepository<MedicalTaskLeftDecisionEntity, MedicalTaskLeftDecisionEntity> {
	List<MedicalTaskLeftDecisionEntity> findAllByMedicalTaskId(Long taskId);
}
