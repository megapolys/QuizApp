package com.example.servingwebcontent.model.entities.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "quiz_task_decisions")
@IdClass(QuizTaskDecisionsEntity.class)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizTaskDecisionsEntity implements Serializable {

	public static QuizTaskDecisionsEntity createNew(Long quizId, Long decisionId) {
		return new QuizTaskDecisionsEntity(quizId, decisionId);
	}

	@Id
	@Column(name = "quiz_task_id")
	Long quizTaskId;

	@Id
	@Column(name = "decisions_id")
	Long decisionsId;

}
