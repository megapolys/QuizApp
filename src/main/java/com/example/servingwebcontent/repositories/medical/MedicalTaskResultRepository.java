package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultEntity;
import org.springframework.data.repository.CrudRepository;

public interface MedicalTaskResultRepository extends CrudRepository<MedicalTaskResultEntity, Long> {
//    void deleteAllByMedicalTask(MedicalTask task);
}
