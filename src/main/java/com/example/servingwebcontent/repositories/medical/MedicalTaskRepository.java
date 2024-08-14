package com.example.servingwebcontent.repositories.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import org.springframework.data.repository.CrudRepository;

public interface MedicalTaskRepository extends CrudRepository<MedicalTask, Long> {
}
