package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.entities.quiz.QuizTaskFullEntity;
import com.example.servingwebcontent.model.quiz.QuizTaskFull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizTaskFullEntityToQuizTaskFullConverter {

	private final FiveVariantTaskEntityToFiveVariantTaskConverter fiveVariantTaskConverter;
	private final YesOrNoTaskEntityToYesOrNoTaskConverter yesOrNoTaskConverter;

	public QuizTaskFull convert(QuizTaskFullEntity entity, List<Long> decisionIds) {
		return QuizTaskFull.builder()
			.id(entity.getId())
			.quizId(entity.getQuizId())
			.position(entity.getPosition())
			.fiveVariantTask(entity.getFiveVariantTaskEntity() == null ? null :
				fiveVariantTaskConverter.convert(entity.getFiveVariantTaskEntity()))
			.yesOrNoTask(entity.getYesOrNoTaskEntity() == null ? null :
				yesOrNoTaskConverter.convert(entity.getYesOrNoTaskEntity()))
			.decisionIds(decisionIds)
			.build();
	}

}
