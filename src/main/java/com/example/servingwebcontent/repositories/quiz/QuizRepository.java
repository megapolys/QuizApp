package com.example.servingwebcontent.repositories.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuizRepository extends CrudRepository<QuizEntity, Long> {

	Optional<QuizEntity> findByShortName(String quizShortName);

	@Query("""
		select new com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity(
					q.id,
					q.name,
					q.shortName,
					count(qt)
				) from QuizEntity q
				left join QuizTaskEntity qt on q.id = qt.quizId
				group by q.id
				order by q.shortName
		""")
	List<QuizWithTaskSizeEntity> getQuizListOrderedByShortName();
}
