package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.model.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUsernameContaining(String name);

    User findByUsername(String name);

    User findByEmail(String email);

    User findByActivationCode(String code);

    User findByUsernameOrEmail(String username, String email);

    User findByRepairPasswordCode(String repairPasswordCode);
}
