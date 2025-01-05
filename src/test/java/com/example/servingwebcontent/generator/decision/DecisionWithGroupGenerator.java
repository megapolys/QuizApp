package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.decision.DecisionWithGroupId;

public class DecisionWithGroupGenerator {

	public static DecisionWithGroupId generate() {
		return DecisionWithGroupId.builder()
			.id(-1L)
			.name("name")
			.description("description")
			.groupId(-201L)
			.build();
	}

	public static DecisionWithGroupId generateNew() {
		return DecisionWithGroupId.builder()
			.name(" name ")
			.description(" description ")
			.groupId(-201L)
			.build();
	}

}
