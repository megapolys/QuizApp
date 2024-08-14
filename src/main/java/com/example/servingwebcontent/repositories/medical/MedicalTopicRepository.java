package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MedicalTopicRepository extends CrudRepository<MedicalTopic, Long> {
    List<MedicalTopic> findAllByOrderByName();

    @Nullable
    MedicalTopic findByName(String name);
}
