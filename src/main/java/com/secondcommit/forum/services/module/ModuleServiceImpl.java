package com.secondcommit.forum.services.module;

import com.secondcommit.forum.dto.ModuleDto;
import com.secondcommit.forum.entities.Module;
import com.secondcommit.forum.repositories.ModuleRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the Module service interface
 */
@Service
public class ModuleServiceImpl implements ModuleService{

    @Autowired
    private ModuleRepository moduleRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    /**
     * Creates a new module from moduleDto
     * @param moduleDto
     * @return ResponseEntity<Module>
     */
    @Override
    public ResponseEntity<?> addModule(ModuleDto moduleDto) {

        //Tests if the module already exists
        Optional<Module> moduleOpt = moduleRepository.findByName(moduleDto.getName());

        if (moduleOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The module " + moduleDto.getName() + " is already registered"));

        //Creates the new module
        Module module = new Module(moduleDto.getName(), moduleDto.getDescription());
        moduleRepository.save(module);

        return ResponseEntity.ok(module);
    }

    /**
     * Method that gets the module
     * @param id
     * @return ResponseEntity<Module>
     */
    @Override
    public ResponseEntity<?> getModule(Long id) {

        //Validates the id
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        if (moduleOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return ResponseEntity.ok(moduleOpt.get());
    }

    /**
     * Updates module from moduleDto
     * @param id
     * @param moduleDto
     * @return ResponseEntity<Module>
     */
    @Override
    public ResponseEntity<?> updateModule(Long id, ModuleDto moduleDto) {

        //Validates the id
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        if (moduleOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        if (moduleDto.getName() != null)
            moduleOpt.get().setName(moduleDto.getName());

        if (moduleDto.getDescription() != null)
            moduleOpt.get().setDescription(moduleDto.getDescription());

        moduleRepository.save(moduleOpt.get());

        return ResponseEntity.ok(moduleOpt.get());
    }

    /**
     * Delete module
     * @param id
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> deleteModule(Long id) {

        //Validates the id
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        if (moduleOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        moduleRepository.delete(moduleOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Module " + id + " deleted with success"));
    }
}
