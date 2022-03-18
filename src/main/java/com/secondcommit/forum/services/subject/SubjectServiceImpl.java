package com.secondcommit.forum.services.subject;

import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.entities.Module;
import com.secondcommit.forum.repositories.ModuleRepository;
import com.secondcommit.forum.repositories.SubjectRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.cloudinary.CloudinaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of the Subject Service Interface
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private final SubjectRepository subjectRepository;

    @Autowired
    private final CloudinaryServiceImpl cloudinary;

    @Autowired
    private final ModuleRepository moduleRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CloudinaryServiceImpl cloudinary,
                              ModuleRepository moduleRepository) {
        this.subjectRepository = subjectRepository;
        this.cloudinary = cloudinary;
        this.moduleRepository = moduleRepository;
    }

    /**
     * Adds a new subject with a name. Modules are optional
     * @param subjectDto
     * @return ResonseEntity
     */
    @Override
    public ResponseEntity<?> addSubject(SubjectDto subjectDto) {
        //Tests if it already exists
        Optional<Subject> subjectOpt = subjectRepository.findByName(subjectDto.getName());

        if (subjectOpt.isPresent())
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The subject " + subjectDto.getName() + " is already registered"));

        Subject subject = new Subject(subjectDto.getName());

        //Tests if it has Modules
        if (subjectDto.getModules() != null){
            Set<Module> modules = new HashSet<>();

            for (Module module : subjectDto.getModules()){
                // Tests the names to see if they already exists
                Optional<Module> moduleOpt = moduleRepository.findByName(module.getName());

                // If it exists, just adds to the set. If it doesn't, creates a new one
                // If it doesn't have a description ignores it
                if (moduleOpt.isPresent()){
                    modules.add(moduleOpt.get());
                }else if (module.getDescription() != null){
                    modules.add(new Module(module.getName(), module.getDescription()));
                }
            }

            subject.setModules(modules);
        }

        subjectRepository.save(subject);

        return ResponseEntity.ok(subject);
    }

    /**
     * Add avatar to the subject
     * @param subject
     * @param avatar (MultipartFile)
     * @return ResponseEntity (with the url of the file uploaded)
     */
    @Override
    public ResponseEntity<?> addAvatarToSubject(Subject subject, MultipartFile avatar) {

        try {
            File file = new File(cloudinary.uploadImage(avatar));
            subject.setAvatar(file);
            subjectRepository.save(subject);

        } catch (Exception e){
            System.out.println("Error : " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to upload avatar"));
        }

        return ResponseEntity.ok(subject.getAvatar().getUrl());
    }

    /**
     * Method that gets the subject
     * @param id
     * @return ResponseEntity<Subject>
     */
    @Override
    public ResponseEntity<?> getSubject(Long id){

        //Validates the id
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        if (subjectOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return ResponseEntity.ok(subjectOpt.get());
    }

    /**
     * Method that updates the subject
     * @param id
     * @param subjectDto
     * @return
     */
    @Override
    public ResponseEntity<?> updateSubject(Long id, SubjectDto subjectDto){

        //Validates the id
        Optional<Subject> subjectOpt = subjectRepository.findById(id);
        if (subjectOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        //Updates name
        if (subjectDto.getName() != null)
            subjectOpt.get().setName(subjectDto.getName());

        //Tests if it has Modules
        if (subjectDto.getModules() != null){
            Set<Module> modules = new HashSet<>();

            for (Module module : subjectDto.getModules()){
                // Tests the names to see if they already exists
                Optional<Module> moduleOpt = moduleRepository.findByName(module.getName());

                // If it exists, just adds to the set. If it doesn't, creates a new one
                // If it doesn't have a description ignores it
                if (moduleOpt.isPresent()){
                    modules.add(moduleOpt.get());
                }else if (module.getDescription() != null){
                    modules.add(new Module(module.getName(), module.getDescription()));
                }
            }

            //Updates modules
            subjectOpt.get().setModules(modules);
        }

        subjectRepository.save(subjectOpt.get());

        return ResponseEntity.ok(subjectOpt.get());
    }

    /**
     * Deletes subject
     * @param id
     * @return ResponseEntity
     */
    @Override
    public ResponseEntity<?> deleteSubject(Long id){

        //Validates the id
        Optional<Subject> subjectOpt = subjectRepository.findById(id);
        if (subjectOpt.isEmpty())
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        subjectRepository.delete(subjectOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Subject " + id + " deleted with success"));
    }
}
