package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Users' entity repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
