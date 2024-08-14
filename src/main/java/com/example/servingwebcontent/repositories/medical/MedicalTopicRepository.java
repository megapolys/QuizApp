package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MedicalTopicRepository extends CrudRepository<MedicalTopicEntity, Long> {
	List<MedicalTopicEntity> findAllByOrderByName();

	@Nullable
	MedicalTopicEntity findByName(String name);
}
