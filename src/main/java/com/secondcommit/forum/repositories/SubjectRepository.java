package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Subjects' entity repository
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    Boolean existsByName(String name);
}
