package com.example.servingwebcontent.converters.quiz;

import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.quiz.task.FiveVariantTask;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class FiveVariantTaskEntityToFiveVariantTaskConverter implements Converter<FiveVariantTaskEntity, FiveVariantTask> {

	@Override
	public FiveVariantTask convert(FiveVariantTaskEntity entity) {
		return FiveVariantTask.builder()
			.id(entity.getId())
			.preQuestionText(entity.getPreQuestionText())
			.questionText(entity.getQuestionText())
			.fileName(entity.getFileName())
			.firstWeight(entity.getFirstWeight())
			.secondWeight(entity.getSecondWeight())
			.thirdWeight(entity.getThirdWeight())
			.fourthWeight(entity.getFourthWeight())
			.fifthWeight(entity.getFifthWeight())
			.build();
	}
}
