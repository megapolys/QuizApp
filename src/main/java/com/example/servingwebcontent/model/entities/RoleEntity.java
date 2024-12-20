package com.example.servingwebcontent.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "role")
@SequenceGenerator(name = "role_gen", sequenceName = "role_id_seq")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class RoleEntity {

	public static RoleEntity createNew(
		String name
	) {
		return new RoleEntity(
			null,
			name
		);
	}

	public static RoleEntity buildExists(
		Long id,
		String name
	) {
		return new RoleEntity(
			id,
			name
		);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_gen")
	Long id;

	@Column(name = "name")
	String name;
}
