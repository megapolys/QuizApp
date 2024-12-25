package com.example.servingwebcontent.converters.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultWithTaskTypeEntity;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResultWithTaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizTaskResultWithTaskTypeEntityToQuizTaskResultWithTaskTypeConverter implements Converter<QuizTaskResultWithTaskTypeEntity, QuizTaskResultWithTaskType> {

	private final FiveVariantTaskEntityToFiveVariantTaskConverter fiveVariantTaskConverter;
	private final YesOrNoTaskEntityToYesOrNoTaskConverter yesOrNoTaskConverter;

	@Override
	public QuizTaskResultWithTaskType convert(QuizTaskResultWithTaskTypeEntity entity) {
		return QuizTaskResultWithTaskType.builder()
			.id(entity.getId())
			.text(entity.getText())
			.variant(entity.getVariant())
			.complete(entity.isComplete())
			.altScore(entity.getAltScore())
			.fiveVariantTask(entity.getFiveVariantTaskEntity() == null ? null :
				fiveVariantTaskConverter.convert(entity.getFiveVariantTaskEntity()))
			.yesOrNoTask(entity.getYesOrNoTaskEntity() == null ? null :
				yesOrNoTaskConverter.convert(entity.getYesOrNoTaskEntity()))
			.build();
	}
}
