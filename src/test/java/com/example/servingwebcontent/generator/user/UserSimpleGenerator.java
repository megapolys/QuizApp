package com.example.servingwebcontent.generator.user;

import com.example.servingwebcontent.model.user.UserSimple;

import java.time.LocalDate;
import java.util.List;

public class UserSimpleGenerator {

	public static UserSimple generate() {
		return UserSimple.builder()
			.id(-1L)
			.username("user")
			.lastName("Иванов")
			.firstName("Иван")
			.middleName("Иванович")
			.email("user@mail.ru")
			.male(Boolean.TRUE)
			.birthday(LocalDate.parse("2000-12-03"))
			.build();
	}

	public static UserSimple generateChanged() {
		return UserSimple.builder()
			.id(-1L)
			.username("user_new")
			.lastName("Курилкин")
			.firstName("Петр")
			.middleName("Иванович")
			.email("user_new@mail.ru")
			.male(Boolean.FALSE)
			.birthday(LocalDate.parse("2000-12-04"))
			.build();
	}

	public static UserSimple generateOther() {
		return
			UserSimple.builder()
				.id(-2L)
				.username("user2")
				.lastName("Иванов")
				.firstName("Иван")
				.middleName("Иванович")
				.email("user2@mail.ru")
				.male(Boolean.TRUE)
				.birthday(LocalDate.parse("2000-12-04"))
				.build();
	}

	public static List<UserSimple> generateList() {
		return List.of(generate(), generateOther());
	}

	public static List<UserSimple> generateOneList() {
		return List.of(generate());
	}

}
