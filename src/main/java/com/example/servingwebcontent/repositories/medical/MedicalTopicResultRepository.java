package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTopicResultEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MedicalTopicResultRepository extends CrudRepository<MedicalTopicResult, Long> {
    void deleteAllByMedicalTopic(MedicalTopic medicalTopic);

    List<MedicalTopicResult> findAllByMedicalTopic(MedicalTopic topic);
}
