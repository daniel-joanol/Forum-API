package com.secondcommit.forum.services.module;

import com.secondcommit.forum.dto.ModuleDto;
import org.springframework.http.ResponseEntity;

/**
 * Module service interface
 */
public interface ModuleService {

    ResponseEntity<?> addModule(Long id, ModuleDto moduleDto);
    ResponseEntity<?> getModule(Long id);
    ResponseEntity<?> updateModule(Long id, ModuleDto moduleDto);
    ResponseEntity<?> deleteModule(Long id);
}
