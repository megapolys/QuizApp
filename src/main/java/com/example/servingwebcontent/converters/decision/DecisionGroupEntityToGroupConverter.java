package com.example.servingwebcontent.converters.decision;

import com.example.servingwebcontent.model.decision.Group;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecisionGroupEntityToGroupConverter implements Converter<DecisionGroupEntity, Group> {

	@Override
	public Group convert(DecisionGroupEntity groupEntity) {
		return Group.builder()
			.id(groupEntity.getId())
			.name(groupEntity.getName())
			.build();
	}
}
