package com.example.servingwebcontent.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "quiz.logger")
public class LoggerProperties {

	/**
	 * Список header, которые необходимо не выводить в лог при получении запроса (request)
	 */
	private List<String> ignoreHeaders = Collections.emptyList();

	/**
	 * Список content-type, для которых не выводится тело (body) запроса (request)
	 */
	private List<MediaType> ignoreContentTypes = Collections.emptyList();

	/**
	 * Количество допустимого размера тела запроса для вывода в лог
	 */
	private long maxBodySizeInByte = 2 * 1024 * 1024;

	/**
	 * Список ключей, для которых необходимо маскировать значение в теле запроса
	 */
	private List<String> maskValueKeys = Collections.emptyList();

	private List<String> ignoreURIPrefix = Collections.emptyList();
}
