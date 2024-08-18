package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.decision.Decision;

import java.util.List;

public class DecisionGenerator {

	public static List<Decision> generateList() {
		return List.of(
			Decision.builder()
				.id(-1L)
				.name("name1")
				.description("desc1")
				.build(),
			Decision.builder()
				.id(-2L)
				.name("name2")
				.description("desc2")
				.build()
		);
	}

}
