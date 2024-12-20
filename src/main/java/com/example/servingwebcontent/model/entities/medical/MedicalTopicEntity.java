package com.example.servingwebcontent.model.entities.medical;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "medical_topic")
@SequenceGenerator(name = "medical_topic_gen", sequenceName = "medical_topic_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class MedicalTopicEntity {

	public static MedicalTopicEntity createNew(String name) {
		return new MedicalTopicEntity(null, name);
	}

	public static MedicalTopicEntity buildExists(Long id, String name) {
		return new MedicalTopicEntity(id, name);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medical_topic_gen")
	Long id;

	@Column(name = "name")
	String name;
}
