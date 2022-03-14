package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Posts' entity repository
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
