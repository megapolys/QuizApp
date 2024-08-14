package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    List<UserEntity> findAll();

    Optional<UserEntity> findByUsername(String name);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByActivationCode(String code);

    boolean existsByRepairPasswordCode(String repairPasswordCode);

    Optional<UserEntity> findByRepairPasswordCode(String repairPasswordCode);
}
