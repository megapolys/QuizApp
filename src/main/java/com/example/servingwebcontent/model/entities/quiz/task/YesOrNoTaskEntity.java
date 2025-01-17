package com.example.servingwebcontent.model.entities.quiz.task;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz_task_yes_or_no")
@SequenceGenerator(name = "quiz_task_yes_or_no_gen", sequenceName = "quiz_task_yes_or_no_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class YesOrNoTaskEntity {

	public static YesOrNoTaskEntity createNew(
		Long taskId,
		String preQuestionText,
		String questionText,
		String fileName,
		Float yesWeight,
		Float noWeight
	) {
		return new YesOrNoTaskEntity(
			taskId,
			preQuestionText,
			questionText,
			fileName,
			yesWeight,
			noWeight
		);
	}

	public static YesOrNoTaskEntity buildExists(
		Long taskId,
		String preQuestionText,
		String questionText,
		String fileName,
		Float yesWeight,
		Float noWeight
	) {
		return new YesOrNoTaskEntity(
			taskId,
			preQuestionText,
			questionText,
			fileName,
			yesWeight,
			noWeight
		);
	}

	@Id
	@Column(name = "task_id")
	Long taskId;

	@Column(name = "pre_question_text")
	String preQuestionText;

	@Column(name = "question_text")
	String questionText;

	@Column(name = "file_name")
	String fileName;

	@Column(name = "yes_weight")
	Float yesWeight;

	@Column(name = "no_weight")
	Float noWeight;
}
