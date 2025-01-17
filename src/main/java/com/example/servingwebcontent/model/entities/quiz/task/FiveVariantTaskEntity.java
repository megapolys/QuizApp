package com.example.servingwebcontent.model.entities.quiz.task;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz_task_five_variant")
@SequenceGenerator(name = "quiz_task_five_variant_gen", sequenceName = "quiz_task_five_variant_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class FiveVariantTaskEntity {

	public static FiveVariantTaskEntity createNew(
		Long taskId,
		String preQuestionText,
		String questionText,
		String fileName,
		Float firstWeight,
		Float secondWeight,
		Float thirdWeight,
		Float fourthWeight,
		Float fifthWeight
	) {
		return new FiveVariantTaskEntity(
			taskId,
			preQuestionText,
			questionText,
			fileName,
			firstWeight,
			secondWeight,
			thirdWeight,
			fourthWeight,
			fifthWeight
		);
	}

	public static FiveVariantTaskEntity buildExists(
		Long taskId,
		String preQuestionText,
		String questionText,
		String fileName,
		Float firstWeight,
		Float secondWeight,
		Float thirdWeight,
		Float fourthWeight,
		Float fifthWeight
	) {
		return new FiveVariantTaskEntity(
			taskId,
			preQuestionText,
			questionText,
			fileName,
			firstWeight,
			secondWeight,
			thirdWeight,
			fourthWeight,
			fifthWeight
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

	@Column(name = "first_weight")
	Float firstWeight;

	@Column(name = "second_weight")
	Float secondWeight;

	@Column(name = "third_weight")
	Float thirdWeight;

	@Column(name = "fourth_weight")
	Float fourthWeight;

	@Column(name = "fifth_weight")
	Float fifthWeight;
}
