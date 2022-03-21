package com.secondcommit.forum.services.subject;

import com.secondcommit.forum.dto.ModuleDto;
import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.dto.SubjectDtoResponse;
import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.entities.Module;
import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.ModuleRepository;
import com.secondcommit.forum.repositories.SubjectRepository;
import com.secondcommit.forum.repositories.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    private final CloudinaryServiceImpl cloudinary;

    @Autowired
    private final ModuleRepository moduleRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository, CloudinaryServiceImpl cloudinary,
                              ModuleRepository moduleRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.cloudinary = cloudinary;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
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

            for (ModuleDto moduleDto : subjectDto.getModules()){
                // Tests the names to see if they already exists
                Optional<Module> moduleOpt = moduleRepository.findByName(moduleDto.getName());

                // If it exists, just adds to the set. If it doesn't, creates a new one
                // If it doesn't have a description ignores it
                if (moduleOpt.isPresent()){
                    modules.add(moduleOpt.get());
                }else if (moduleDto.getDescription() != null){
                    modules.add(new Module(moduleDto.getName(), moduleDto.getDescription()));
                }
            }

            subject.setModules(modules);
        }

        //Upload image to Cloudinary
        try {
            File file = new File(cloudinary.uploadImage(subjectDto.getAvatar()));
            subject.setAvatar(file);

        } catch (Exception e){
            System.err.println("Error : " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to upload avatar"));
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

        //Upload image to Cloudinary
        try {
            File file = new File(cloudinary.uploadImage(avatar));
            subject.setAvatar(file);
            subjectRepository.save(subject);

        } catch (Exception e){
            System.err.println("Error : " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse("Failed to upload avatar"));
        }

        return ResponseEntity.ok(subject.getAvatar().getUrl());
    }

    /**
     * Method that gets all data in the subject
     * @param id
     * @return ResponseEntity (ok: Subject, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> getSubject(Long id){

        //Gets the id
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        return ResponseEntity.ok(subjectOpt.get());
    }

    /**
     * Method to get the subjects that the user are allowed to
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: set(subjectDtoResponse), no content)
     */
    @Override
    public ResponseEntity<?> getSubjectsAllowed(String username) {

        //Gets user
        Optional<User> userOpt = userRepository.findByUsername(username);

        Set<SubjectDtoResponse> response = new HashSet<>();
        for (Subject subject : userOpt.get().getHasAccess())
            response.add(new SubjectDtoResponse(subject.getId(), subject.getName()));

        if (response.size() == 0)
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(response);
    }

    /**
     * Method that updates the subject
     * @param id
     * @param subjectDto
     * @return ResponseEntity (ok: Subject, bad request: messageResponse)
     */
    @Override
    public ResponseEntity<?> updateSubject(Long id, SubjectDto subjectDto){

        //Gets the subject
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        //Updates name
        if (subjectDto.getName() != null)
            subjectOpt.get().setName(subjectDto.getName());

        //Tests if it has Modules
        if (subjectDto.getModules() != null){
            Set<Module> modules = new HashSet<>();

            for (ModuleDto moduleDto : subjectDto.getModules()){
                // Tests the names to see if they already exists
                Optional<Module> moduleOpt = moduleRepository.findByName(moduleDto.getName());

                // If it exists, just adds to the set. If it doesn't, creates a new one
                // If it doesn't have a description ignores it
                if (moduleOpt.isPresent()){
                    modules.add(moduleOpt.get());
                }else if (moduleDto.getDescription() != null){
                    modules.add(new Module(moduleDto.getName(), moduleDto.getDescription()));
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

        //Gets the subject
        Optional<Subject> subjectOpt = subjectRepository.findById(id);

        subjectRepository.delete(subjectOpt.get());

        return ResponseEntity.ok().body(new MessageResponse("Subject " + id + " deleted with success"));
    }
}
