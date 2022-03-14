package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Answers' entity repository
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
