package com.example.servingwebcontent.model.entities.quiz.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "quiz_result")
@SequenceGenerator(name = "quiz_result_gen", sequenceName = "quiz_result_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizResultEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_result_gen")
	Long id;

	@Column(name = "quiz_id")
	Long quizId;

	@Column(name = "user_id")
	Long userId;

	@Column(name = "complete")
	boolean complete;

	@Column(name = "complete_date")
	Instant completeDate;
}
