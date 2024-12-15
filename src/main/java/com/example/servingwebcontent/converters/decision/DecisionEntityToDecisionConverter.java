package com.example.servingwebcontent.converters.decision;

import com.example.servingwebcontent.model.decision.Decision;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DecisionEntityToDecisionConverter implements Converter<DecisionEntity, Decision> {

	@Override
	public Decision convert(DecisionEntity entity) {
		return Decision.builder()
			.id(entity.getId())
			.name(entity.getName())
			.description(entity.getDescription())
			.build();
	}
}
