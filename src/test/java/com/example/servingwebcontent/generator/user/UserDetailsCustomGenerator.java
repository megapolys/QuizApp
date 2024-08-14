package com.example.servingwebcontent.generator.user;

import com.example.servingwebcontent.model.user.UserDetailsCustom;

public class UserDetailsCustomGenerator {

	public static UserDetailsCustom generate() {
		return UserDetailsCustom.builder()
			.id(-1L)
			.username("user")
			.password("pass")
			.build();
	}

}
