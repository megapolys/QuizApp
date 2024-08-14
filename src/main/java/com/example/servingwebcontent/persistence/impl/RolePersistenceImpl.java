package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.exceptions.RoleNotFoundException;
import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.entities.RoleEntity;
import com.example.servingwebcontent.persistence.RolePersistence;
import com.example.servingwebcontent.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolePersistenceImpl implements RolePersistence {

	private final RoleRepository roleRepository;
	private final ConversionService conversionService;

	@Override
	public Role findRoleByName(String name) {
		RoleEntity roleEntity = roleRepository.findByName(name)
			.orElseThrow(() -> RoleNotFoundException.byName(name));
		return conversionService.convert(roleEntity, Role.class);

	}
}
