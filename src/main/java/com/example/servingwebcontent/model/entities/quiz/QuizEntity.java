package com.example.servingwebcontent.model.entities.quiz;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "quiz")
@SequenceGenerator(name = "quiz_gen", sequenceName = "quiz_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizEntity {

	public static QuizEntity createNew(
			String name,
			String shortName
	) {
		return new QuizEntity(
				null,
				name,
				shortName
		);
	}

	public static QuizEntity buildExisting(
			Long id,
			String name,
			String shortName
	) {
		return new QuizEntity(
				id,
				name,
				shortName
		);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_gen")
	Long id;

	@Column(name = "name")
	String name;

	@Column(name = "short_name")
	String shortName;
}
