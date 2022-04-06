package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Subjects' entity repository
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAll();
    Boolean existsByName(String name);
    Optional<Subject> findByName(String name);

}
