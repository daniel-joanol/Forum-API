
package com.secondcommit.forum.repositories;

import com.secondcommit.forum.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Modules' entity repository
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
