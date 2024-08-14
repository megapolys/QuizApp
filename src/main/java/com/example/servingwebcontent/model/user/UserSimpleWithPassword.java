package com.example.servingwebcontent.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
public class UserSimpleWithPassword extends UserSimple {

	String password;

	String password2;
}

