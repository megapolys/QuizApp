package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DecisionRepository extends CrudRepository<DecisionEntity, Long> {
	DecisionEntity findByName(String name);

	List<DecisionEntity> findAllByOrderByName();

//    List<DecisionEntity> findAllByGroup(DecisionGroup group);
}
