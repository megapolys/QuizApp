package com.example.servingwebcontent.converters.medical;

import com.example.servingwebcontent.model.entities.medical.MedicalTaskEntity;
import com.example.servingwebcontent.model.medical.MedicalTaskFull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalTaskEntityToMedicalTaskFullConverter {

	public MedicalTaskFull convert(MedicalTaskEntity entity, List<Long> leftDecisions, List<Long> rightDecisions) {
		return MedicalTaskFull.builder()
			.id(entity.getId())
			.topicId(entity.getTopicId())
			.name(entity.getName())
			.unit(entity.getUnit())
			.leftLeft(entity.getLeftLeft())
			.leftMid(entity.getLeftMid())
			.rightMid(entity.getRightMid())
			.rightRight(entity.getRightRight())
			.leftDecisions(leftDecisions)
			.rightDecisions(rightDecisions)
			.build();
	}
}
