package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.decision.DecisionByTask;
import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DecisionRepository extends JpaRepository<DecisionEntity, Long> {
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

	@Query("""
		select new com.example.servingwebcontent.model.decision.DecisionByTask(
			d.id,
			d.name,
			d.description,
			qt.id
		)
		from DecisionEntity d
		join QuizTaskDecisionsEntity qtd on qtd.decisionsId = d.id
		join QuizTaskEntity qt on qt.id = qtd.quizTaskId
		where qt.quizId = :quizId
		""")
	List<DecisionByTask> findAllByQuizId(Long quizId);

	@Query("""
		select new com.example.servingwebcontent.model.decision.DecisionWithGroup(
			d.id,
			d.name,
			d.description,
			dg.name,
			mt.id
		)
		from DecisionEntity d
		left join DecisionGroupEntity dg on dg.id = d.groupId
		join MedicalTaskLeftDecisionEntity mtld on mtld.decisionsId = d.id
		join MedicalTaskEntity mt on mt.id = mtld.medicalTaskId
		where mt.topicId = :medicalTopicId
		""")
	List<DecisionWithGroup> findAllLeftDecisionsByMedicalTopicId(Long medicalTopicId);

	@Query("""
		select new com.example.servingwebcontent.model.decision.DecisionWithGroup(
			d.id,
			d.name,
			d.description,
			dg.name,
			mt.id
		)
		from DecisionEntity d
		left join DecisionGroupEntity dg on dg.id = d.groupId
		join MedicalTaskRightDecisionEntity mtrd on mtrd.decisionsId = d.id
		join MedicalTaskEntity mt on mt.id = mtrd.medicalTaskId
		where mt.topicId = :medicalTopicId
		""")
	List<DecisionWithGroup> findAllRightDecisionsByMedicalTopicId(Long medicalTopicId);
}
