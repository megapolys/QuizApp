package com.example.servingwebcontent.model.entities.quiz;

import com.example.servingwebcontent.model.entities.quiz.task.FiveVariantTaskEntity;
import com.example.servingwebcontent.model.entities.quiz.task.YesOrNoTaskEntity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizTaskFullEntity {

	Long id;

	Long quizId;

	int position;

	Long countDecisions;

	FiveVariantTaskEntity fiveVariantTaskEntity;

	YesOrNoTaskEntity yesOrNoTaskEntity;
}
