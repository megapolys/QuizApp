package com.example.servingwebcontent.converters.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizTaskResultEntity;
import com.example.servingwebcontent.model.quiz.result.QuizTaskResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class QuizTaskResultEntityToQuizTaskResultConverter implements Converter<QuizTaskResultEntity, QuizTaskResult> {

	@Override
	public QuizTaskResult convert(QuizTaskResultEntity entity) {
		return QuizTaskResult.builder()
			.id(entity.getId())
			.text(entity.getText())
			.variant(entity.getVariant())
			.complete(entity.isComplete())
			.altScore(entity.getAltScore())
			.build();
	}
}
