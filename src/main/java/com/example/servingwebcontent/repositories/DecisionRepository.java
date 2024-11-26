package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends CrudRepository<DecisionEntity, Long> {
	List<DecisionEntity> findAll();

	List<DecisionEntity> findAllByGroupIdIsNullOrderByName();

	List<DecisionEntity> findAllByGroupIdIsNotNullOrderByName();

	@Transactional
	@Modifying
	@Query("""
			update DecisionEntity d
			set d.groupId = null
			where d.groupId = :groupId
		""")
	void deleteGroup(Long groupId);

	boolean existsByName(String name);

	Optional<DecisionEntity> findByName(String name);

	@Query("""
		select qtd.decisionsId
		from QuizTaskDecisionsEntity qtd
		where qtd.quizTaskId = :taskId
		""")
	List<Long> findAllIdsByTaskId(Long taskId);
}
