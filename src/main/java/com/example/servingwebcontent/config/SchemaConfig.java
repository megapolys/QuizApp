package com.example.servingwebcontent.config;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
public class SchemaConfig implements BeanPostProcessor {

	@Value("${spring.liquibase.liquibase-schema}")
	private String schemaName;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (StringUtils.isNotBlank(schemaName) && bean instanceof DataSource dataSource) {
			try (Connection conn = dataSource.getConnection();
				 Statement statement = conn.createStatement()) {
				log.info("Going to create Liquibase schema '{}' if not exists.", schemaName);
				statement.execute("create schema if not exists " + schemaName);
			} catch (SQLException e) {
				throw new RuntimeException("Failed to create schema '" + schemaName + "'", e);
			}
		}
		return bean;
	}
}
