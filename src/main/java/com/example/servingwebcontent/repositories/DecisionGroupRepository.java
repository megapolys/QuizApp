package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DecisionGroupRepository extends CrudRepository<DecisionGroupEntity, Long> {
	List<DecisionGroupEntity> findAllByOrderByName();

	boolean existsByName(String name);

	Optional<DecisionGroupEntity> findByName(String name);
}
