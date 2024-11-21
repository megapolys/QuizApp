package com.example.servingwebcontent.model.entities.quiz;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz_task_decisions")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizTaskDecisionsEntity {

	@Id
	@Column(name = "quiz_task_id")
	Long quizTaskId;

	@Id
	@Column(name = "decisions_id")
	Long decisionsId;
}
