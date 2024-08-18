package com.example.servingwebcontent.repositories.quiz.custom.impl;

import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;
import com.example.servingwebcontent.repositories.quiz.custom.QuizCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuizCustomRepositoryImpl implements QuizCustomRepository {

	private final EntityManager entityManager;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<QuizWithTaskSizeEntity> getQuizListOrderedByShortName() {
		String jpqlQuery = """
				select new com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity(
					q.id,
					q.name,
					q.shortName,
					count(qt)
				) from QuizEntity q
				left join QuizTaskEntity qt on q.id = qt.quizId
				group by q.id
				order by q.shortName
			""";
		TypedQuery<QuizWithTaskSizeEntity> query = entityManager.createQuery(jpqlQuery, QuizWithTaskSizeEntity.class);
		return query.getResultList();
	}
}
