package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;

public class DecisionWithGroupGenerator {

	public static DecisionWithGroup generate() {
		return DecisionWithGroup.builder()
			.id(-1L)
			.name("name")
			.description("description")
			.groupId(-201L)
			.build();
	}

	public static DecisionWithGroup generateNew() {
		return DecisionWithGroup.builder()
			.name(" name ")
			.description(" description ")
			.groupId(-201L)
			.build();
	}

}
