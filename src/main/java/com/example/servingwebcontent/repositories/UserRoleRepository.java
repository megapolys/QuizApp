package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRoleEntity> {

	List<UserRoleEntity> findAllByUserId(Long userId);
}
