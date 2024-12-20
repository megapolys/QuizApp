package com.example.servingwebcontent.converters.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTopicEntity;
import com.example.servingwebcontent.model.medical.MedicalTopic;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class MedicalTopicEntityToMedicalTopicConverter implements Converter<MedicalTopicEntity, MedicalTopic> {
	@Override
	public MedicalTopic convert(MedicalTopicEntity entity) {
		return MedicalTopic.builder()
			.id(entity.getId())
			.name(entity.getName())
			.build();
	}
}
