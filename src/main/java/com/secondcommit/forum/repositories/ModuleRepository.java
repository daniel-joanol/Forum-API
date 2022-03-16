
package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Modules' entity repository
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    Optional<Module> findByName(String name);
}
