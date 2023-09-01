package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.domain.medical.MedicalTopic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MedicalTopicRepository extends CrudRepository<MedicalTopic, Long> {
    List<MedicalTopic> findAllByOrderByName();

    @Nullable
    MedicalTopic findByName(String name);
}
