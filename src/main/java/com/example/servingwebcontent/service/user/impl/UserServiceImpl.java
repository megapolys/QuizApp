package com.example.servingwebcontent.service.user.impl;

import com.example.servingwebcontent.exceptions.UserAlreadyExistsByEmailException;
import com.example.servingwebcontent.exceptions.UserAlreadyExistsByUsernameException;
import com.example.servingwebcontent.exceptions.UserNotFoundException;
import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import com.example.servingwebcontent.persistence.UserPersistence;
import com.example.servingwebcontent.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	@Value("${spring.mail.from}")
	private String from;
	@Value("${domain.name}")
	private String domainName;

	private final UserPersistence userPersistence;
	private final JavaMailSender mailSender;
	private final PasswordEncoder passwordEncoder;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserSimple> findAll() {
		return userPersistence.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserSimple getSimpleUserById(Long userId) {
		return userPersistence.findSimpleUserById(userId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateUser(Long userId, UserSimple newUser) {
		checkUser(newUser);
		newUser.setId(userId);
		userPersistence.save(newUser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void register(UserSimpleWithPassword user) {
		checkUser(user);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPassword2(null);
		String activationCode = userPersistence.create(user);
		if (StringUtils.isNotBlank(user.getEmail())) {
			final String message = String.format(
				"Здравствуйте, %s \n" +
					"Добро пожаловать на Bodymind State. Пожалуйста перейдите по ссылки для подтверждения регистрации: %s/activate/%s",
				user.getUsername(), domainName, activationCode
			);
			final SimpleMailMessage simpleMessage = new SimpleMailMessage();
			simpleMessage.setFrom(from);
			simpleMessage.setTo(user.getEmail());
			simpleMessage.setSubject("Код активации");
			simpleMessage.setText(message);
			mailSender.send(simpleMessage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean activateUser(String code) {
		return userPersistence.activateUser(code);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean repairPassword(String email) {
		try {
			String repairPasswordCode = UUID.randomUUID().toString();
			UserSimple user = userPersistence.setRepairPasswordCodeByEmail(email, repairPasswordCode);
			final String message = String.format(
				"Здравствуйте, %s \n" +
					"Ссылка на восстановление пароля для Bodymind State: %s/repairPassword/%s",
				user.getUsername(), domainName, repairPasswordCode
			);
			final SimpleMailMessage simpleMessage = new SimpleMailMessage();
			simpleMessage.setFrom(from);
			simpleMessage.setTo(user.getEmail());
			simpleMessage.setSubject("Восстановление пароля");
			simpleMessage.setText(message);
			mailSender.send(simpleMessage);
			return true;
		} catch (UserNotFoundException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean existsByRepairPasswordCode(String repairPasswordCode) {
		return userPersistence.userExistsByRepairPasswordCode(repairPasswordCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePassword(String repairPasswordCode, String password) {
		userPersistence.updatePassword(repairPasswordCode, passwordEncoder.encode(password));
	}

	private void checkUser(UserSimple user) {
		Long userIdByUsername = userPersistence.findUserIdByUsername(user.getUsername());
		if (userIdByUsername != null && !Objects.equals(userIdByUsername, user.getId())) {
			throw UserAlreadyExistsByUsernameException.byUsername(user.getUsername());
		}
		Long userIdByEmail = userPersistence.findUserIdByEmail(user.getEmail());
		if (userIdByEmail != null && !Objects.equals(userIdByEmail, user.getId())) {
			throw UserAlreadyExistsByEmailException.byEmail(user.getEmail());
		}
	}
}
