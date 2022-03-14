package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Users' entity repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
