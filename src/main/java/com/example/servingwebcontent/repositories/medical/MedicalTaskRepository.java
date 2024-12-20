package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import com.example.servingwebcontent.model.medical.MedicalTaskWithDecisionsSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicalTaskRepository extends JpaRepository<MedicalTaskEntity, Long> {

	List<MedicalTaskEntity> findAllByTopicId(Long id);

	Optional<MedicalTaskEntity> findByNameAndTopicId(String name, Long topicId);

	@Query("""
		select new com.example.servingwebcontent.model.medical.MedicalTaskWithDecisionsSize(
			t.id,
			t.topicId,
			t.name,
			t.unit,
			count(tld.decisionsId) filter (where tld.medicalTaskId is not null),
			count(trd.decisionsId) filter (where trd.medicalTaskId is not null)
		) from MedicalTaskEntity t
		left join MedicalTaskLeftDecisionEntity tld on tld.medicalTaskId = t.id
		left join MedicalTaskRightDecisionEntity trd on trd.medicalTaskId = t.id
		where t.topicId = :medicalTopicId
		group by t.id
		order by t.name
		""")
	List<MedicalTaskWithDecisionsSize> getMedicalTaskList(Long medicalTopicId);
}
