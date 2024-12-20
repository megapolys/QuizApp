package com.example.servingwebcontent.model.entities.medical.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "medical_task_result")
@SequenceGenerator(name = "medical_task_result_gen", sequenceName = "medical_task_result_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MedicalTaskResultEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_task_result_gen")
	Long id;

	@Column(name = "medical_task_id")
	Long taskId;

	@Column(name = "topic_result_id")
	Long topicResultId;

	@Column(name = "value")
	Float value;

	@Column(name = "alt_score")
	Float altScore;
}
