package com.example.servingwebcontent.generator.role;

import com.example.servingwebcontent.model.entities.RoleEntity;

public class RoleEntityGenerator {

	public static RoleEntity generate() {
		return RoleEntity.buildExists(
			-1L,
			"USER"
		);
	}

}
