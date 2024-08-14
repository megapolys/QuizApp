package com.example.servingwebcontent.service;

import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;

import java.util.List;

public interface UserService {

	/**
	 * Получение списка пользователей
	 *
	 * @return Список пользователей
	 */
	List<UserSimple> findAll();


	/**
	 * Получение своего профиля
	 *
	 * @param userId - Идентификатор автора запроса
	 *
	 * @return Профиль пользователя
	 */
	UserSimple getSimpleUserById(Long userId);

	/**
	 * Изменение своего профиля
	 *
	 * @param userId  - Идентификатор автора запроса
	 * @param newUser - Новые данные профиля
	 */
	void updateUser(Long userId, UserSimple newUser);

	/**
	 * Регистрация нового пользователя
	 *
	 * @param user - Данные профиля
	 */
	void register(UserSimpleWithPassword user);

	/**
	 * Активация профиля
	 *
	 * @param code - Код активации
	 *
	 * @return true - если активация успешна, иначе false
	 */
	boolean activateUser(String code);

	/**
	 * Получение ссылки на восстановление пароля
	 *
	 * @param email - Почта
	 *
	 * @return true - если ссылка отправлена на почту, false - почта не найдена
	 */
	boolean repairPassword(String email);

	/**
	 * Поиск профиля по коду восстановления пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 *
	 * @return true - если код найден, иначе false
	 */
	boolean existsByRepairPasswordCode(String repairPasswordCode);


	/**
	 * Изменение пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 */
	void updatePassword(String repairPasswordCode, String password);
}
