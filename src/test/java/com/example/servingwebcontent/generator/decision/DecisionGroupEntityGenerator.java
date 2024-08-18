package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;

import java.util.List;

public class DecisionGroupEntityGenerator {

	public static DecisionGroupEntity generate() {
		return DecisionGroupEntity.buildExisting(
			-201L,
			"group_name"
		);
	}

	public static DecisionGroupEntity generateNew() {
		return DecisionGroupEntity.createNew("new_group");
	}

	public static List<DecisionGroupEntity> generateList() {
		return List.of(
			DecisionGroupEntity.buildExisting(
				-201L,
				"group1"
			),
			DecisionGroupEntity.buildExisting(
				-202L,
				"group2"
			)
		);
	}

}
