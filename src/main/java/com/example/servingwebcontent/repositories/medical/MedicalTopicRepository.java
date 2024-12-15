package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MedicalTopicRepository extends JpaRepository<MedicalTopicEntity, Long> {

	@Query("""
		select new com.example.servingwebcontent.model.medical.MedicalTopicWithTaskSize(
			m.id,
			m.name,
			count(mt.id)
		) from MedicalTopicEntity m
		left join MedicalTaskEntity mt on mt.topicId = m.id
		group by m.id
		order by m.name
		""")
	List<MedicalTopicWithTaskSize> findAllByOrderByName();

	@Nullable
	MedicalTopicEntity findByName(String name);

	boolean existsByName(String name);
}
