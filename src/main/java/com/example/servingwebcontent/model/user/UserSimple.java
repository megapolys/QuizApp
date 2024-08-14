package com.example.servingwebcontent.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserSimple {

	Long id;

	String username;

	String firstName;

	String lastName;

	String middleName;

	String email;

	LocalDate birthday;

	Boolean male;
}
