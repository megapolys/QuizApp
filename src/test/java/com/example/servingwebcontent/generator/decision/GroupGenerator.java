package com.example.servingwebcontent.generator.decision;

import com.example.servingwebcontent.model.decision.Group;

public class GroupGenerator {

	public static Group generate() {
		return Group.builder()
			.id(-201L)
			.name("group_name")
			.build();
	}

}
