package com.example.servingwebcontent.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "usr")
@SequenceGenerator(name = "usr_gen", sequenceName = "usr_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UserEntity {

	public static UserEntity createNew(
		String activationCode,
		boolean active,
		String email,
		String firstName,
		String lastName,
		String middleName,
		String password,
		String username,
		String repairPasswordCode,
		LocalDate birthday,
		Boolean male
	) {
		return new UserEntity(
			null,
			activationCode,
			active,
			email,
			firstName,
			lastName,
			middleName,
			password,
			username,
			repairPasswordCode,
			birthday,
			male
		);
	}

	public static UserEntity buildExisting(
		Long id,
		String activationCode,
		boolean active,
		String email,
		String firstName,
		String lastName,
		String middleName,
		String password,
		String username,
		String repairPasswordCode,
		LocalDate birthday,
		Boolean male
	) {
		return new UserEntity(
			id,
			activationCode,
			active,
			email,
			firstName,
			lastName,
			middleName,
			password,
			username,
			repairPasswordCode,
			birthday,
			male
		);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usr_gen")
	Long id;

	@Column(name = "activation_code")
	String activationCode;

	@Column(name = "active")
	boolean active;

	@Column(name = "email")
	String email;

	@Column(name = "first_name")
	String firstName;

	@Column(name = "last_name")
	String lastName;

	@Column(name = "middle_name")
	String middleName;

	@Column(name = "password")
	String password;

	@Column(name = "username")
	String username;

	@Column(name = "repair_password_code")
	String repairPasswordCode;

	@Column(name = "birthday")
	LocalDate birthday;

	@Column(name = "male")
	Boolean male;
}
