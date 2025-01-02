package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultEntity;
import com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultWithTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalTaskResultRepository extends JpaRepository<MedicalTaskResultEntity, Long> {

	void deleteAllByTaskId(Long taskId);

	@Query("""
		select new com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultWithTaskEntity(
			mtr.id,
			mt,
			mtr.topicResultId,
			mtr.value,
			mtr.altScore
		) from MedicalTaskResultEntity mtr
		join MedicalTaskEntity mt on mt.id = mtr.taskId
		where mtr.topicResultId = :topicResultId
		""")
	List<MedicalTaskResultWithTaskEntity> findAllByTopicResultId(Long topicResultId);

	void deleteAllByTopicResultId(Long medicalResultId);
}
