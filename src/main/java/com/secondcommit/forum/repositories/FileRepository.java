package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Files' entity repository
 */
@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    Boolean existsByCloudinaryId(String cloudinaryId);
    File findByCloudinaryId(String cloudinaryId);
}
