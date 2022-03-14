package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Roles' entity repository
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
