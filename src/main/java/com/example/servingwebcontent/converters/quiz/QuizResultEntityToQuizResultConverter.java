package com.example.servingwebcontent.converters.quiz;

import com.example.servingwebcontent.model.entities.quiz.result.QuizResultEntity;
import com.example.servingwebcontent.model.quiz.result.QuizResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class QuizResultEntityToQuizResultConverter implements Converter<QuizResultEntity, QuizResult> {

	@Override
	public QuizResult convert(QuizResultEntity entity) {
		return QuizResult.builder()
			.id(entity.getId())
			.quizId(entity.getQuizId())
			.userId(entity.getUserId())
			.complete(entity.isComplete())
			.completeDate(entity.getCompleteDate())
			.build();
	}
}
