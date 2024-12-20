package com.example.servingwebcontent.generator.quiz;

import com.example.servingwebcontent.model.entities.quiz.QuizWithTaskSizeEntity;

import java.util.List;

public class QuizWithTaskSizeEntityGenerator {

	public static List<QuizWithTaskSizeEntity> generateList() {
		return List.of(
			QuizWithTaskSizeEntity.buildExisting(
				-1L,
				"name1",
				"shortName1",
				0L
			),
			QuizWithTaskSizeEntity.buildExisting(
				-2L,
				"name2",
				"shortName2",
				3L
			)
		);
	}

}
