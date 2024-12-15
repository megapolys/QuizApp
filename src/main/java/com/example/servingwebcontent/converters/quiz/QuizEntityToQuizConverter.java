package com.example.servingwebcontent.converters.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizEntity;
import com.example.servingwebcontent.model.quiz.Quiz;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class QuizEntityToQuizConverter implements Converter<QuizEntity, Quiz> {

	@Override
	public Quiz convert(QuizEntity entity) {
		return Quiz.builder()
				.id(entity.getId())
				.shortName(entity.getShortName())
				.name(entity.getName())
				.build();
	}
}
