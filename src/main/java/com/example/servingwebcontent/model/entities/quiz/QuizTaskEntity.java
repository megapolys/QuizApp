package com.example.servingwebcontent.model.entities.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz_task")
@SequenceGenerator(name = "quiz_task_gen", sequenceName = "quiz_task_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizTaskEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_task_gen")
	Long id;

	@Column(name = "quiz_id")
	Long quizId;

	@Column(name = "position")
	int position;

	@Column(name = "quiz_task_five_variant_id")
	Long quizTaskFiveVariantId;

	@Column(name = "quiz_task_yes_or_no_id")
	Long quizTaskYesOrNoId;
}