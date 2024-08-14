package com.example.servingwebcontent.service;

import com.example.servingwebcontent.exceptions.UserNotFoundException;
import com.example.servingwebcontent.model.ResultType;
import com.example.servingwebcontent.model.UserResult;
import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import com.example.servingwebcontent.persistence.UserPersistence;
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
	public UserResult updateUser(Long userId, UserSimple newUser) {
		UserResult result = checkUser(newUser);
		if (result != null) {
			return result;
		}
		newUser.setId(userId);
		userPersistence.save(newUser);
		return new UserResult(ResultType.SUCCESS, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public UserResult register(UserSimpleWithPassword user) {
		UserResult result = checkUser(user);
		if (result != null) {
			return result;
		}
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
		return new UserResult(ResultType.SUCCESS, null);
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

	private UserResult checkUser(UserSimple user) {
		Long userIdByUsername = userPersistence.findUserIdByUsername(user.getUsername());
		if (userIdByUsername != null && !Objects.equals(userIdByUsername, user.getId())) {
			return new UserResult(ResultType.USERNAME_FOUND, null);
		}
		Long userIdByEmail = userPersistence.findUserIdByEmail(user.getEmail());
		if (userIdByEmail != null && !Objects.equals(userIdByEmail, user.getId())) {
			return new UserResult(ResultType.EMAIL_FOUND, null);
		}
		return null;
	}
}
