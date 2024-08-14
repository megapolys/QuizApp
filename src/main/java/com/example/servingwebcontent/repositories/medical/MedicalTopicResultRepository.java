package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTopicResultEntity;
import org.springframework.data.repository.CrudRepository;

public interface MedicalTopicResultRepository extends CrudRepository<MedicalTopicResultEntity, Long> {
//    void deleteAllByMedicalTopic(MedicalTopic medicalTopic);

//    List<MedicalTopicResultEntity> findAllByMedicalTopic(MedicalTopic topic);
}
