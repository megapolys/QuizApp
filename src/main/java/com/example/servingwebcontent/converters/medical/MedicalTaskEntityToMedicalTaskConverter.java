package com.example.servingwebcontent.converters.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import com.example.servingwebcontent.model.medical.MedicalTask;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class MedicalTaskEntityToMedicalTaskConverter implements Converter<MedicalTaskEntity, MedicalTask> {
	@Override
	public MedicalTask convert(MedicalTaskEntity entity) {
		return MedicalTask.builder()
			.id(entity.getId())
			.topicId(entity.getTopicId())
			.name(entity.getName())
			.unit(entity.getUnit())
			.leftLeft(entity.getLeftLeft())
			.leftMid(entity.getLeftMid())
			.rightMid(entity.getRightMid())
			.rightRight(entity.getRightRight())
			.build();
	}
}
