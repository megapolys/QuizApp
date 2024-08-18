package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;

import java.util.List;

public class DecisionEntityGenerator {

	public static List<DecisionEntity> generateUngroupedList() {
		return List.of(
			DecisionEntity.buildExisting(
				-1L,
				"name1",
				"desc1",
				null
			),
			DecisionEntity.buildExisting(
				-2L,
				"name2",
				"desc2",
				null
			)
		);
	}

	public static List<DecisionEntity> generateList() {
		return List.of(
			DecisionEntity.buildExisting(
				-3L,
				"name3",
				"desc3",
				-201L
			),
			DecisionEntity.buildExisting(
				-4L,
				"name4",
				"desc4",
				-201L
			)
		);
	}

	public static DecisionEntity generateNew() {
		return DecisionEntity.createNew(
			"name",
			"description",
			-201L
		);
	}

	public static DecisionEntity generate() {
		return DecisionEntity.buildExisting(
			-1L,
			"name",
			"description",
			-201L
		);
	}
}
