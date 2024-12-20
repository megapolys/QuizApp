package com.example.servingwebcontent.model.entities.quiz.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz_task_result")
@SequenceGenerator(name = "quiz_task_result_gen", sequenceName = "quiz_task_result_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizTaskResultEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_task_result_gen")
	Long id;

	@Column(name = "task_id")
	Long taskId;

	@Column(name = "quiz_id")
	Long quizResultId;

	@Column(name = "complete")
	boolean complete;

	@Column(name = "variant")
	String variant;

	@Column(name = "alt_score")
	Float altScore;

	@Column(name = "text")
	String text;
}
