package com.example.servingwebcontent.model.entities.medical.result;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "medical_topic_result")
@SequenceGenerator(name = "medical_topic_result_gen", sequenceName = "medical_topic_result_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MedicalTopicResultEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_topic_result_gen")
	Long id;

	@Column(name = "medical_topic_id")
	Long medicalTopicId;

	@Column(name = "user_id")
	Long userId;

	@Column(name = "complete_date")
	Instant completeDate;

	@Column(name = "last_update_date")
	Instant lastUpdateDate;
}
