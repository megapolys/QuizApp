package com.example.servingwebcontent.converters.user;

import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.entities.UserEntity;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserEntityToUserDetailsCustomConverter {

	public UserDetailsCustom convert(UserEntity entity, Set<Role> authorities) {
		return UserDetailsCustom.builder()
			.id(entity.getId())
			.username(entity.getUsername())
			.password(entity.getPassword())
			.roles(authorities)
			.active(entity.isActive())
			.build();
	}
}
