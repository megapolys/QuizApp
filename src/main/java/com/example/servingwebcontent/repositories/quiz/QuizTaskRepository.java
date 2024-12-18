package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QuizTaskRepository extends JpaRepository<QuizTaskEntity, Long> {

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity(
					qt.id,
					qt.quizId,
					qt.position,
					count(qtd) filter (where qtd.quizTaskId is not null),
					fvt,
					ynt
				) from QuizTaskEntity qt
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				left join QuizTaskDecisionsEntity qtd on qt.id = qtd.quizTaskId
				where qt.quizId = :quizId
				group by qt.id, fvt.id, ynt.id
				order by qt.position
		""")
	List<QuizTaskFullEntity> findAllFullByQuizId(Long quizId);

	List<QuizTaskEntity> findAllByQuizId(Long quizId);

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity(
					qt.id,
					qt.quizId,
					qt.position,
					count(qtd) filter (where qtd.quizTaskId is not null),
					fvt,
					ynt
				) from QuizTaskEntity qt
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				left join QuizTaskDecisionsEntity qtd on qt.id = qtd.quizTaskId
				where qt.id = :taskId
				group by qt.id, fvt.id, ynt.id
		""")
	Optional<QuizTaskFullEntity> findFullByTaskId(Long taskId);

	boolean existsByQuizIdAndPosition(Long quizId, Integer position);

	@Transactional
	@Modifying
	@Query("""
		update QuizTaskEntity t
		set t.position = t.position + 1
		where t.quizId = :quizId and t.position >= :position
		""")
	void updateByQuizIdAndPositionGatherOrEquals(Long quizId, Integer position);
}
