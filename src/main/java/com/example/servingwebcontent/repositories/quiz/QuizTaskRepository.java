package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuizTaskRepository extends CrudRepository<QuizTaskEntity, Long> {

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity(
					qt.id,
					qt.quizId,
					qt.position,
					count(qtd),
					fvt,
					ynt
				) from QuizTaskEntity qt
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				left join QuizTaskDecisionsEntity qtd on qt.id = qtd.quizTaskId
				where qt.quizId = :quizId
				group by qt.id
				order by qt.position
		""")
	List<QuizTaskFullEntity> findAllByQuizId(Long quizId);


	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity(
					qt.id,
					qt.quizId,
					qt.position,
					count(qtd),
					fvt,
					ynt
				) from QuizTaskEntity qt
				left join FiveVariantTaskEntity fvt on qt.quizTaskFiveVariantId = fvt.id
				left join YesOrNoTaskEntity ynt on qt.quizTaskYesOrNoId = ynt.id
				left join QuizTaskDecisionsEntity qtd on qt.id = qtd.quizTaskId
				where qt.id = :taskId
				group by qt.id
		""")
	Optional<QuizTaskFullEntity> findByTaskId(Long taskId);
//    QuizTaskEntity findByPositionAndQuiz(int pos, Quiz quiz);
}
