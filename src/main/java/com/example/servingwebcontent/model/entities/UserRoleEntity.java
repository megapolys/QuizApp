package com.example.servingwebcontent.model.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "usr_role")
@IdClass(UserRoleEntity.class)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class UserRoleEntity implements Serializable {

	public static UserRoleEntity createNew(
		Long userId,
		Long roleId
	) {
		return new UserRoleEntity(
			userId,
			roleId
		);
	}

	@Id
	@Column(name = "user_id")
	Long userId;

	@Id
	@Column(name = "role_id")
	Long roleId;
}
