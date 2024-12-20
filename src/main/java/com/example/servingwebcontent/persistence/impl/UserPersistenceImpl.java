package com.example.servingwebcontent.persistence.impl;

import com.example.servingwebcontent.converters.user.RoleEntityToRoleConverter;
import com.example.servingwebcontent.converters.user.UserEntityToUserDetailsCustomConverter;
import com.example.servingwebcontent.converters.user.UserEntityToUserSimpleConverter;
import com.example.servingwebcontent.exceptions.user.RoleNotFoundException;
import com.example.servingwebcontent.exceptions.user.UserNotFoundException;
import com.example.servingwebcontent.model.Role;
import com.example.servingwebcontent.model.entities.RoleEntity;
import com.example.servingwebcontent.model.entities.UserEntity;
import com.example.servingwebcontent.model.entities.UserRoleEntity;
import com.example.servingwebcontent.model.user.UserDetailsCustom;
import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import com.example.servingwebcontent.persistence.UserPersistence;
import com.example.servingwebcontent.repositories.RoleRepository;
import com.example.servingwebcontent.repositories.UserRepository;
import com.example.servingwebcontent.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.servingwebcontent.consts.Consts.USER_ROLE_NAME;

@Service
@RequiredArgsConstructor
public class UserPersistenceImpl implements UserPersistence {

	private final UserRepository userRepository;
	private final UserRoleRepository userRoleRepository;
	private final UserEntityToUserDetailsCustomConverter userEntityToUserDetailsCustomConverter;
	private final UserEntityToUserSimpleConverter userEntityToUserSimpleConverter;
	private final RoleRepository roleRepository;
	private final RoleEntityToRoleConverter roleEntityToRoleConverter;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserSimple> findAll() {
		return userRepository.findAll().stream()
			.map(userEntityToUserSimpleConverter::convert)
			.toList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserSimple findSimpleUserById(Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> UserNotFoundException.byId(userId));
		return userEntityToUserSimpleConverter.convert(userEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(UserSimple newUser) {
		UserEntity userEntity = userRepository.findById(newUser.getId())
			.orElseThrow(() -> UserNotFoundException.byId(newUser.getId()));
		userEntity.setUsername(newUser.getUsername());
		userEntity.setLastName(newUser.getLastName());
		userEntity.setFirstName(newUser.getFirstName());
		userEntity.setBirthday(newUser.getBirthday());
		userEntity.setEmail(newUser.getEmail());
		userEntity.setMale(newUser.getMale());
		userRepository.save(userEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String create(UserSimpleWithPassword user) {
		String activationCode = UUID.randomUUID().toString();
		UserEntity userEntity = userRepository.save(UserEntity.createNew(
			activationCode,
			false,
			user.getEmail(),
			user.getFirstName(),
			user.getLastName(),
			user.getMiddleName(),
			user.getPassword(),
			user.getUsername(),
			null,
			user.getBirthday(),
			user.getMale()
		));
		RoleEntity userRole = roleRepository.findByName(USER_ROLE_NAME)
			.orElseThrow(() -> RoleNotFoundException.byName(USER_ROLE_NAME));
		userRoleRepository.save(UserRoleEntity.createNew(userEntity.getId(), userRole.getId()));
		return activationCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetailsCustom findUserDetailsByUsername(String username) {
		return userRepository.findByUsername(username)
			.map(this::getUserWithRoles)
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long findUserIdByUsername(String username) {
		return userRepository.findByUsername(username)
			.map(UserEntity::getId)
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserDetailsCustom findUserDetailsByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(this::getUserWithRoles)
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long findUserIdByEmail(String email) {
		return userRepository.findByEmail(email)
			.map(UserEntity::getId)
			.orElse(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean activateUser(String code) {
		Optional<UserEntity> optionalUserEntity = userRepository.findByActivationCode(code);
		if (optionalUserEntity.isEmpty()) {
			return false;
		}
		UserEntity userEntity = optionalUserEntity.get();
		userEntity.setActivationCode(null);
		userEntity.setActive(true);
		userRepository.save(userEntity);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserSimple setRepairPasswordCodeByEmail(String email, String repairPasswordCode) {
		UserEntity userEntity = userRepository.findByEmail(email)
			.orElseThrow(() -> UserNotFoundException.byEmail(email));
		userEntity.setRepairPasswordCode(repairPasswordCode);
		userRepository.save(userEntity);
		return userEntityToUserSimpleConverter.convert(userEntity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean userExistsByRepairPasswordCode(String repairPasswordCode) {
		return userRepository.existsByRepairPasswordCode(repairPasswordCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePassword(String repairPasswordCode, String passwordHash) {
		UserEntity userEntity = userRepository.findByRepairPasswordCode(repairPasswordCode)
			.orElseThrow(() -> UserNotFoundException.byRepairPasswordCode(repairPasswordCode));
		userEntity.setPassword(passwordHash);
		userEntity.setRepairPasswordCode(null);
		userRepository.save(userEntity);
	}

	private UserDetailsCustom getUserWithRoles(UserEntity userEntity) {
		List<Long> roleIds = userRoleRepository.findAllByUserId(userEntity.getId()).stream()
			.map(UserRoleEntity::getRoleId)
			.toList();
		Set<Role> roles = roleRepository.findAllById(roleIds).stream()
			.map(roleEntityToRoleConverter::convert)
			.collect(Collectors.toSet());
		return userEntityToUserDetailsCustomConverter.convert(userEntity, roles);
	}
}
