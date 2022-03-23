package com.secondcommit.forum.services.module;

import com.secondcommit.forum.dto.ModuleDto;
import com.secondcommit.forum.entities.Module;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.repositories.ModuleRepository;
import com.secondcommit.forum.repositories.SubjectRepository;
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

    @Autowired
    private SubjectRepository subjectRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository, SubjectRepository subjectRepository) {
        this.moduleRepository = moduleRepository;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Method to create a new module from moduleDto
     * @param moduleDto
     * @return ResponseEntity (ok: module, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> addModule(Long id, ModuleDto moduleDto) {

        //Gets module
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        //Tests if the module already exists
        Optional<Module> moduleOpt = moduleRepository.findByName(moduleDto.getName());

        if (moduleOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The module " + moduleDto.getName() + " is already registered"));

        //Creates the new module
        Module module = new Module(moduleDto.getName(), moduleDto.getDescription());
        subjectOpt.get().addModule(module);
        moduleRepository.save(module);
        subjectRepository.save(subjectOpt.get());

        return ResponseEntity.ok(module.getDtoFromModule());
    }

    /**
     * Method to get the all module data
     * @param id
     * @return ResponseEntity (ok: module, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getModule(Long id) {

        //Gets the module
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        return ResponseEntity.ok(moduleOpt.get().getDtoFromModule());
    }

    /**
     * Method to update module from moduleDto
     * @param id
     * @param moduleDto
     * @return ResponseEntity(ok: module, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateModule(Long id, ModuleDto moduleDto) {

        //Gets module
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        //Updates the module
        if (moduleDto.getName() != null)
            moduleOpt.get().setName(moduleDto.getName());

        if (moduleDto.getDescription() != null)
            moduleOpt.get().setDescription(moduleDto.getDescription());

        moduleRepository.save(moduleOpt.get());

        return ResponseEntity.ok(moduleOpt.get().getDtoFromModule());
    }

    /**
     * Method to delete module
     * @param id
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> deleteModule(Long id) {

        //Gets module
        Optional<Module> moduleOpt = moduleRepository.findById(id);

        moduleRepository.delete(moduleOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Module " + id + " deleted with success"));
    }
}
