package com.example.servingwebcontent.repository.decision;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

public class DecisionRepositoryTest_DeleteGroup_Test extends DecisionRepositoryTest {

	@Test
	@Sql(value = {"/decision/insert_decision_group.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/decision/insert_decision.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(value = {"/clean_all_tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void whenFoundThenReturnList() {
		long groupId = -201L;

		decisionRepository.deleteGroup(groupId);

		decisionRepository.findAllById(List.of(3L, 4L))
			.forEach(entity -> then(entity.getGroupId()).isNull());
	}
}
