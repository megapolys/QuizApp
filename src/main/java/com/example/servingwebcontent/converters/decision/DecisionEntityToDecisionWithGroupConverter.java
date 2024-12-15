package com.example.servingwebcontent.converters.decision;

import com.example.servingwebcontent.model.decision.DecisionWithGroup;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DecisionEntityToDecisionWithGroupConverter implements Converter<DecisionEntity, DecisionWithGroup> {

	@Override
	public DecisionWithGroup convert(DecisionEntity entity) {
		return DecisionWithGroup.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.groupId(entity.getGroupId())
			.build();
	}
}
