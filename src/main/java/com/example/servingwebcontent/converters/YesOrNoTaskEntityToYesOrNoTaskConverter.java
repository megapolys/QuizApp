package com.example.servingwebcontent.converters;

import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import com.example.servingwebcontent.model.quiz.task.YesOrNoTask;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class YesOrNoTaskEntityToYesOrNoTaskConverter implements Converter<YesOrNoTaskEntity, YesOrNoTask> {

	@Override
	public YesOrNoTask convert(YesOrNoTaskEntity entity) {
		return YesOrNoTask.builder()
			.preQuestionText(entity.getPreQuestionText())
			.questionText(entity.getQuestionText())
			.fileName(entity.getFileName())
			.yesWeight(entity.getYesWeight())
			.noWeight(entity.getNoWeight())
			.build();
	}
}
