package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Users' entity repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users  u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    //TODO: Implement query to look for an exact username

    //@Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?0", nativeQuery = true)
    //User findByEmailAddress(String emailAddress);
}
