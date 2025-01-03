package com.example.servingwebcontent.model.medical.result;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class MedicalTopicResultUpdateCommandDto {

	/**
	 * Идентификатор результата топика анализов
	 */
	@NotNull
	Long topicResultId;

	/**
	 * key - идентификатор результата таска
	 * value - значение
	 */
	Map<Long, Float> results;

}
