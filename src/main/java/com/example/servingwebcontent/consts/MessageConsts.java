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

	String GROUP_WITH_SAME_NAME_ALREADY_EXISTS = "Такое имя группы уже занято.";

	String GROUP_NAME_IS_EMPTY = "Пустое название группы!";

	String DECISION_WITH_SAME_NAME_ALREADY_EXISTS = "Такое имя решения уже занято.";

	String DECISION_NAME_IS_EMPTY = "Пустое имя решения!";

	String QUIZ_WITH_SHORT_NAME_ALREADY_EXISTS = "Данное кратное наименование уже занято";

	String MEDICAL_TOPIC_ALREADY_EXISTS_BY_NAME = "Анализ с таким именем уже существует";

	//	SUCCESS

	String CHANGES_COMPLETE_SUCCESSFUL = "Изменения успешно сохранены";

	String PROFILE_SUCCESSFUL_ACTIVATED = "Пользователь успешно активирован";

	String REPAIR_PASSWORD_CODE_SENT = "Ссылка на восстановление пароля выслана на вашу электронную почту.";

	String PASSWORD_SUCCESSFUL_CHANGED = "Пароль успешно обновлен.";

	String GROUP_SUCCESSFUL_ADDED = "Группа добавлена";

	String GROUP_SUCCESSFUL_CHANGED = "Группа обновлена";

	String GROUP_SUCCESSFUL_DELETED = "Группа удалена";

	String DECISION_SUCCESSFUL_ADDED = "Решение добавлено";

	String DECISION_SUCCESSFUL_CHANGED = "Решение обновлено";

	String DECISION_SUCCESSFUL_DELETED = "Решение удалено";

}
