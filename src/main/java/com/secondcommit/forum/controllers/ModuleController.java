package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.ModuleDto;
import com.secondcommit.forum.repositories.ModuleRepository;
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
    private final ModuleRepository moduleRepository;

    public ModuleController(ModuleServiceImpl moduleService, ModuleRepository moduleRepository){
        this.moduleService = moduleService;
        this.moduleRepository = moduleRepository;
    }

    /**
     * Method to create a new module from moduleDto. ADMIN only
     * @param moduleDto
     * @return ResponseEntity (ok: Module, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new module. Authentication required (ADMIN)")
    public ResponseEntity<?> newModule(@RequestBody ModuleDto moduleDto){

        if (moduleDto.getName() == null || moduleDto.getDescription() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return moduleService.addModule(moduleDto);
    }

    /**
     * Method to get all data in a module
     * @param id
     * @return ResponseEntity (ok: Module, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all module data. Authentication required (user)")
    public ResponseEntity<?> getModule(@PathVariable Long id){

        //Validates ID
        if (!moduleRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return moduleService.getModule(id);
    }

    /**
     * Method that updates the module. ADMIN only
     * @param id
     * @param moduleDto
     * @return ResponseEntity (ok: Module, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation("Updates module data. Authentication required (ADMIN)")
    public ResponseEntity<?> updateModule(@PathVariable Long id, @RequestBody ModuleDto moduleDto){

        //Validates DTO
        if (moduleDto.getName() == null && moduleDto.getDescription() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates ID
        if (!moduleRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return moduleService.updateModule(id, moduleDto);
    }

    /**
     * Deletes module. ADMIN only
     * @param id
     * @return ResponseEntity (MessageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes module. Authentication required (ADMIN)")
    public ResponseEntity<?> deleteModule(@PathVariable Long id){

        //Validates ID
        if (!moduleRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return moduleService.deleteModule(id);
    }
}
