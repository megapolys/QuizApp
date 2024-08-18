package com.example.servingwebcontent.generator.quiz;

import com.example.servingwebcontent.model.quiz.QuizWithTaskSize;

import java.util.List;

public class QuizWithTaskSizeGenerator {

	public static List<QuizWithTaskSize> generateList() {
		return List.of(
			QuizWithTaskSize.builder()
				.id(-1L)
				.name("name1")
				.shortName("shortName1")
				.size(0L)
				.build(),
			QuizWithTaskSize.builder()
				.id(-2L)
				.name("name2")
				.shortName("shortName2")
				.size(3L)
				.build()
		);
	}

}
