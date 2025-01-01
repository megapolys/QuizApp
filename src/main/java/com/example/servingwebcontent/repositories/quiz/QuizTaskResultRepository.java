package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultWithTaskTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizTaskResultRepository extends JpaRepository<QuizTaskResultEntity, Long> {

	void deleteByTaskId(Long taskId);

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultWithTaskTypeEntity(
					qtr.id,
					qt,
					qtr.complete,
					qtr.variant,
					qtr.altScore,
					qtr.text,
					fvt,
					ynt
				) from QuizTaskResultEntity qtr
				join QuizTaskEntity qt on qt.id = qtr.taskId
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				where qtr.quizResultId = :quizResultId
				order by qt.position
		""")
	List<QuizTaskResultWithTaskTypeEntity> findAllByQuizResultId(Long quizResultId);

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultWithTaskTypeEntity(
					qtr.id,
					qt,
					qtr.complete,
					qtr.variant,
					qtr.altScore,
					qtr.text,
					fvt,
					ynt
				) from QuizTaskResultEntity qtr
				join QuizTaskEntity qt on qt.id = qtr.taskId
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				where qtr.id = :quizTaskResultId
		""")
	Optional<QuizTaskResultWithTaskTypeEntity> getQuizTaskResultById(Long quizTaskResultId);

	void deleteAllByQuizResultId(Long quizResultId);
}
