package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.domain.medical.MedicalTask;
import com.example.servingwebcontent.domain.medical.result.MedicalTaskResult;
import org.springframework.data.repository.CrudRepository;

public interface MedicalTaskResultRepository extends CrudRepository<MedicalTaskResult, Long> {
    void deleteAllByMedicalTask(MedicalTask task);
}
