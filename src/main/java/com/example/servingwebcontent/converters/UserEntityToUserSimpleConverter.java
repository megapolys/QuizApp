package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.entities.UserEntity;
import com.example.servingwebcontent.model.user.UserSimple;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserEntityToUserSimpleConverter implements Converter<UserEntity, UserSimple> {

	@Override
	public UserSimple convert(UserEntity userEntity) {
		return UserSimple.builder()
			.id(userEntity.getId())
			.username(userEntity.getUsername())
			.firstName(userEntity.getFirstName())
			.lastName(userEntity.getLastName())
			.middleName(userEntity.getMiddleName())
			.email(userEntity.getEmail())
			.birthday(userEntity.getBirthday())
			.male(userEntity.isMale())
			.build();
	}
}
