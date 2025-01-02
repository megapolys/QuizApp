package com.example.servingwebcontent.converters.medical;

import com.example.servingwebcontent.model.entities.medical.result.MedicalTopicResultEntity;
import com.example.servingwebcontent.model.medical.result.MedicalTopicResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class MedicalTopicResultEntityToMedicalTopicResultConverter implements Converter<MedicalTopicResultEntity, MedicalTopicResult> {

	@Override
	public MedicalTopicResult convert(MedicalTopicResultEntity entity) {
		return MedicalTopicResult.builder()
			.id(entity.getId())
			.topicId(entity.getTopicId())
			.userId(entity.getUserId())
			.completeDate(entity.getCompleteDate())
			.lastUpdateDate(entity.getLastUpdateDate())
			.build();
	}
}
