package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Subjects' entity repository
 */
@Repository
public interface SubjectRepository extends JpaRepository<Role, Long> {
}
