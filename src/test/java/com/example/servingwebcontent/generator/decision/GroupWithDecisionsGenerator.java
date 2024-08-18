package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.decision.GroupWithDecisions;

import java.util.List;

public class GroupWithDecisionsGenerator {

	public static List<GroupWithDecisions> generateList() {
		return List.of(
			GroupWithDecisions.builder()
				.id(-201L)
				.name("group1")
				.decisions(List.of(
					Decision.builder()
						.id(-3L)
						.name("name3")
						.description("desc3")
						.build(),
					Decision.builder()
						.id(-4L)
						.name("name4")
						.description("desc4")
						.build()
				))
				.build(),
			GroupWithDecisions.builder()
				.id(-202L)
				.name("group2")
				.decisions(List.of())
				.build()
		);
	}

}
