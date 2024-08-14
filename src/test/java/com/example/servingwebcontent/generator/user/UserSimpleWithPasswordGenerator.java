package com.example.servingwebcontent.generator.user;

import com.example.servingwebcontent.model.user.UserSimpleWithPassword;

import java.time.LocalDate;

public class UserSimpleWithPasswordGenerator {

	public static UserSimpleWithPassword generate() {
		return UserSimpleWithPassword.builder()
			.id(-1L)
			.username("user")
			.lastName("Иванов")
			.firstName("Иван")
			.middleName("Иванович")
			.email("user@mail.ru")
			.male(Boolean.TRUE)
			.birthday(LocalDate.parse("2000-12-03"))
			.password("pass")
			.password2("pass")
			.build();
	}

}
