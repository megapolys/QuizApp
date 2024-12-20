package com.example.servingwebcontent.generator.user;

import com.example.servingwebcontent.model.entities.UserEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UserEntityGenerator {

	public static UserEntity generate() {
		return UserEntity.buildExisting(
			-1L,
			null,
			true,
			"user@mail.ru",
			"Иван",
			"Иванов",
			"Иванович",
			"$2a$08$liW7gbEa.O1PCzTtyh9ekuxf2E7ZVRY0jUY/.oRg6CNbC6P4JUze6",
			"user",
			null,
			LocalDate.parse("2000-12-03"),
			true
		);
	}

	public static UserEntity generateInactive() {
		return UserEntity.buildExisting(
			-1L,
			UUID.randomUUID().toString(),
			false,
			"user@mail.ru",
			"Иван",
			"Иванов",
			"Иванович",
			"$2a$08$liW7gbEa.O1PCzTtyh9ekuxf2E7ZVRY0jUY/.oRg6CNbC6P4JUze6",
			"user",
			null,
			LocalDate.parse("2000-12-03"),
			true
		);
	}

	public static UserEntity generateWithRepairPasswordCode() {
		return UserEntity.buildExisting(
			-1L,
			null,
			true,
			"user@mail.ru",
			"Иван",
			"Иванов",
			"Иванович",
			"$2a$08$liW7gbEa.O1PCzTtyh9ekuxf2E7ZVRY0jUY/.oRg6CNbC6P4JUze6",
			"user",
			UUID.randomUUID().toString(),
			LocalDate.parse("2000-12-03"),
			true
		);
	}

	public static UserEntity generateChanged() {
		return UserEntity.buildExisting(
			-1L,
			null,
			true,
			"user_new@mail.ru",
			"Петр",
			"Курилкин",
			"Иванович",
			"$2a$08$liW7gbEa.O1PCzTtyh9ekuxf2E7ZVRY0jUY/.oRg6CNbC6P4JUze6",
			"user_new",
			null,
			LocalDate.parse("2000-12-04"),
			false
		);
	}

	public static UserEntity generateOther() {
		return UserEntity.buildExisting(
			-2L,
			null,
			true,
			"user2@mail.ru",
			"Иван",
			"Иванов",
			"Иванович",
			"password_hash2",
			"user2",
			null,
			LocalDate.parse("2000-12-04"),
			true
		);
	}

	public static List<UserEntity> generateList() {
		return List.of(generate(), generateOther());
	}

	public static List<UserEntity> generateOneList() {
		return List.of(generate());
	}

}
