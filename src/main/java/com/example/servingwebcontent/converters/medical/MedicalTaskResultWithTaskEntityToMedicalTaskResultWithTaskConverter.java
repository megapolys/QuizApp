package com.example.servingwebcontent.converters.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTaskResultWithTaskEntity;
import com.example.servingwebcontent.model.medical.result.MedicalTaskResultWithTask;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicalTaskResultWithTaskEntityToMedicalTaskResultWithTaskConverter implements Converter<MedicalTaskResultWithTaskEntity, MedicalTaskResultWithTask> {

	private final MedicalTaskEntityToMedicalTaskConverter taskConverter;

	@Override
	public MedicalTaskResultWithTask convert(MedicalTaskResultWithTaskEntity entity) {
		return MedicalTaskResultWithTask.builder()
			.id(entity.getId())
			.task(taskConverter.convert(entity.getTask()))
			.topicResultId(entity.getTopicResultId())
			.value(entity.getValue())
			.build();
	}
}
