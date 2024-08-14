package com.example.servingwebcontent.model.entities.medical;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "medical_task")
@SequenceGenerator(name = "medical_task_gen", sequenceName = "medical_task_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
// При добавлении полей необходимо дорабатывать метод клонирование MedicalTopicService.clone()
public class MedicalTaskEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_task_gen")
	Long id;

	@Column(name = "name")
	String name;

	@Column(name = "unit")
	String unit;

	@Column(name = "topic_id")
	Long topicId;

	@Column(name = "left_left")
	Float leftLeft;

	@Column(name = "left_mid")
	Float leftMid;

	@Column(name = "right_mid")
	Float rightMid;

	@Column(name = "right_right")
	Float rightRight;
}
