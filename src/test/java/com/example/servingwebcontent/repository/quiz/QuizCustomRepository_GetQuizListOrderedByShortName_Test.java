package com.example.servingwebcontent.repository.quiz;

import com.example.servingwebcontent.generator.quiz.QuizWithTaskSizeEntityGenerator;
import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

public class QuizCustomRepository_GetQuizListOrderedByShortName_Test extends QuizRepositoryTest {

	@Test
	@Sql(value = {"/quiz/insert_quiz.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/quiz/insert_quiz_task.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/clean_all_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void whenFoundThenReturnList() {
		List<QuizWithTaskSizeEntity> expectedQuizList = QuizWithTaskSizeEntityGenerator.generateList();
		List<QuizWithTaskSizeEntity> actualQuizList = quizRepository.getQuizListOrderedByShortName();

		then(actualQuizList).isEqualTo(expectedQuizList);
	}

	@Test
	void whenNotFoundThenReturnEmpty() {
		List<QuizWithTaskSizeEntity> actualQuizList = quizRepository.getQuizListOrderedByShortName();

		then(actualQuizList).isEmpty();
	}
}
