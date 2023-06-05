package com.example.servingwebcontent.repositories;

import com.example.servingwebcontent.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByUsernameContaining(String name);

    User findByUsername(String name);

    User findByEmail(String email);
}
