package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.domain.medical.MedicalTask;
import com.example.servingwebcontent.domain.medical.MedicalTopic;
import org.springframework.data.repository.CrudRepository;

public interface MedicalTaskRepository extends CrudRepository<MedicalTask, Long> {
}
