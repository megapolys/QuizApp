package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.entities.RoleEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class RoleEntityToRoleConverter implements Converter<RoleEntity, Role> {

	@Override
	public Role convert(RoleEntity entity) {
		return Role.builder()
			.id(entity.getId())
			.name(entity.getName())
			.build();
	}
}
