package com.example.servingwebcontent.model.medical.result;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MedicalTopicResultUpdateCommandDto {

	/**
	 * Идентификатор результата топика анализов
	 */
	@NotNull
	Long topicResultId;

	/**
	 * Список результатов тасков
	 */
	List<MedicalTaskResultDto> results;
}
