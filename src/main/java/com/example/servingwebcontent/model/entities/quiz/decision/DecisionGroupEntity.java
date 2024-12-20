package com.example.servingwebcontent.model.entities.quiz.decision;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "decision_group")
@SequenceGenerator(name = "decision_group_gen", sequenceName = "decision_group_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DecisionGroupEntity {

	public static DecisionGroupEntity createNew(String name) {
		return new DecisionGroupEntity(
			null,
			name
		);
	}

	public static DecisionGroupEntity buildExisting(Long id, String name) {
		return new DecisionGroupEntity(
			id,
			name
		);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "decision_group_gen")
	Long id;

	@Column(name = "name")
	String name;
}
