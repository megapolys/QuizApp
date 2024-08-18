package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.decision.GroupWithDecisions;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionEntity;
import com.example.servingwebcontent.model.entities.quiz.decision.DecisionGroupEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DecisionGroupEntityToGroupWithDecisionsConverter {

	private final DecisionEntityToDecisionConverter decisionEntityToDecisionConverter;

	public GroupWithDecisions convert(DecisionGroupEntity groupEntity, List<DecisionEntity> decisionEntity) {
		return GroupWithDecisions.builder()
			.id(groupEntity.getId())
			.name(groupEntity.getName())
			.decisions(decisionEntity == null ? List.of() : decisionEntity.stream()
				.map(decisionEntityToDecisionConverter::convert)
				.toList())
			.build();
	}
}
