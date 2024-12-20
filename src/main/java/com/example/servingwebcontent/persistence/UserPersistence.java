package com.example.servingwebcontent.persistence;

import com.example.servingwebcontent.model.user.UserSimple;
import com.example.servingwebcontent.model.user.UserSimpleWithPassword;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserPersistence {

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
	UserSimple findSimpleUserById(Long userId);

	/**
	 * Сохранение новых данных профиля
	 *
	 * @param newUser - Новые данные профиля
	 */
	void save(UserSimple newUser);

	/**
	 * Создание нового профиля
	 *
	 * @param user - Данные профиля
	 *
	 * @return Код активации
	 */
	String create(UserSimpleWithPassword user);

	/**
	 * Получение UserDetails по username
	 *
	 * @param username - Наименование пользователя
	 *
	 * @return Профиль пользователя
	 */
	UserDetails findUserDetailsByUsername(String username);

	/**
	 * Получение идентификатора пользователя по username
	 *
	 * @param username - Наименование пользователя
	 *
	 * @return Идентификатор профиля пользователя
	 */
	Long findUserIdByUsername(String username);

	/**
	 * Получение UserDetails по почте
	 *
	 * @param email - Почта
	 *
	 * @return Профиль пользователя
	 */
	UserDetails findUserDetailsByEmail(String email);

	/**
	 * Получение идентификатора пользователя по почте
	 *
	 * @param email - Наименование пользователя
	 *
	 * @return Идентификатор профиля пользователя
	 */
	Long findUserIdByEmail(String email);

	/**
	 * Активация профиля
	 *
	 * @param code - Код активации
	 *
	 * @return true - если активация успешна, иначе false
	 */
	boolean activateUser(String code);

	/**
	 * Установление профилю код восстановления пароля
	 *
	 * @param email              - Почта
	 * @param repairPasswordCode - Код восстановления пароля
	 *
	 * @return true - если ссылка отправлена на почту, false - почта не найдена
	 */
	UserSimple setRepairPasswordCodeByEmail(String email, String repairPasswordCode);

	/**
	 * Получение идентификатора профиля по коду восстановления пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 *
	 * @return true - если профиль с кодом найден, иначе false
	 */
	boolean userExistsByRepairPasswordCode(String repairPasswordCode);

	/**
	 * Изменение пароля
	 *
	 * @param repairPasswordCode - Код восстановления пароля
	 */
	void updatePassword(String repairPasswordCode, String passwordHash);
}
