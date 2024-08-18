package com.example.servingwebcontent.model.entities.quiz.decision;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "decision")
@SequenceGenerator(name = "decision_gen", sequenceName = "decision_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DecisionEntity {

	public static DecisionEntity createNew(
		String name,
		String description,
		Long groupId
	) {
		return new DecisionEntity(
			null,
			name,
			description,
			groupId
		);
	}

	public static DecisionEntity buildExisting(
		Long id,
		String name,
		String description,
		Long groupId
	) {
		return new DecisionEntity(
			id,
			name,
			description,
			groupId
		);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "decision_gen")
	Long id;

	@Column(name = "name")
	String name;

	@Column(name = "description")
	String description;

	@Column(name = "group_id")
	Long groupId;
}
