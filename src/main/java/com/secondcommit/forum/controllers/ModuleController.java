package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.ModuleDto;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.module.ModuleServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to manage the Module CRUD methods
 */
@RestController
@RequestMapping("/api/module")
public class ModuleController {

    private final ModuleServiceImpl moduleService;

    public ModuleController(ModuleServiceImpl moduleService){
        this.moduleService = moduleService;
    }

    /**
     * Creates a new module from moduleDto
     * @param moduleDto
     * @return ResponseEntity<Module>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new module")
    public ResponseEntity<?> newModule(@RequestBody ModuleDto moduleDto){

        if (moduleDto.getName() == null || moduleDto.getDescription() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return moduleService.addModule(moduleDto);
    }

    /**
     * Gets all module data
     * @param id
     * @return ResponseEntity<Module>
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all module data")
    public ResponseEntity<?> getModule(@PathVariable Long id){
        return moduleService.getModule(id);
    }

    /**
     * Method that updates the module
     * @param id
     * @param moduleDto
     * @return ResponseEntity<Module>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation("Updates module data")
    public ResponseEntity<?> updateModule(@PathVariable Long id, @RequestBody ModuleDto moduleDto){

        if (moduleDto.getName() == null && moduleDto.getDescription() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return moduleService.updateModule(id, moduleDto);
    }

    /**
     * Deletes module
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes module")
    public ResponseEntity<?> deleteModule(@PathVariable Long id){
        return moduleService.deleteModule(id);
    }
}
