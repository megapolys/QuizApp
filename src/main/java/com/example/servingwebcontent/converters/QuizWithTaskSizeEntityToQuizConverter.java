package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;
import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class QuizWithTaskSizeEntityToQuizConverter implements Converter<QuizWithTaskSizeEntity, QuizWithTaskSize> {

	@Override
	public QuizWithTaskSize convert(QuizWithTaskSizeEntity quizEntity) {
		return QuizWithTaskSize.builder()
			.id(quizEntity.getId())
			.name(quizEntity.getName())
			.shortName(quizEntity.getShortName())
			.size(quizEntity.getSize())
			.build();
	}
}
