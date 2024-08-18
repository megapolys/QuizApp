package com.example.servingwebcontent.model.entities.quiz;

import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class QuizWithTaskSizeEntity {

	public static QuizWithTaskSizeEntity buildExisting(
		Long id,
		String name,
		String shortName,
		Long size
	) {
		return new QuizWithTaskSizeEntity(
			id,
			name,
			shortName,
			size
		);
	}

	@Id
	Long id;

	String name;

	String shortName;

	/**
	 * Количество тасков
	 */
	Long size;
}
