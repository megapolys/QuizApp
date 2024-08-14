package com.example.servingwebcontent.consts;

public interface MessageConsts {

	//	EXCEPTION

	String CAPTCHA_EXCEPTION_MESSAGE = "Ошибка капчи";

	String REQUIRE_ALL_FIELDS = "Необходимо заполнить все поля";

	String DIFFERENT_PASSWORDS = "Пароли не совпадают";

	String PROFILE_WITH_SAME_USERNAME_ALREADY_EXISTS = "Пользователь с таким логином уже существует!";

	String PROFILE_WITH_SAME_EMAIL_ALREADY_EXISTS = "Пользователь с такой почтой уже существует!";

	String LOGIN_EXCEPTION = "Ошибочка, похоже логин или пароль не подходят";

	String LOGIN_LOGOUT_EXCEPTION = "Вы покинули нас...";

	String ACTIVATION_CODE_NOT_FOUND = "Код активации не найден";

	String EMAIL_REQUIRED = "Введите свою почту";

	String PROFILE_BY_EMAIL_NOT_FOUND = "Пользователя с данной почтой не обнаружено.";

	String REPAIR_PASSWORD_CODE_NOT_FOUND = "Невалидная ссылка на восстановление пароля";

	//	SUCCESS

	String CHANGES_COMPLETE_SUCCESSFUL = "Изменения успешно сохранены";

	String PROFILE_SUCCESSFUL_ACTIVATED = "Пользователь успешно активирован";

	String REPAIR_PASSWORD_CODE_SENT = "Ссылка на восстановление пароля выслана на вашу электронную почту.";

	String PASSWORD_SUCCESSFUL_CHANGED = "Пароль успешно обновлен.";

}
