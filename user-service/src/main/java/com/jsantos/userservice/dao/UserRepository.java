package com.jsantos.userservice.dao;

import com.jsantos.userservice.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUserId(String userId);

    void deleteByUserId(String userId);
}
