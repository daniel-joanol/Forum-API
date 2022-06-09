package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.repositories.SubjectRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.subject.SubjectServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 *  Controller to manage the User CRUD
 */
@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    private SubjectServiceImpl subjectService;
    private SubjectRepository subjectRepository;

    public SubjectController(SubjectServiceImpl subjectService, SubjectRepository subjectRepository) {
        this.subjectService = subjectService;
        this.subjectRepository = subjectRepository;
    }

    /**
     * Creates a new subject from subjectDto. ADMIN only
     * @param subjectDto
     * @return ResponseEntity (ok: Subject, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new subject. Authentication required (ADMIN)")
    public ResponseEntity<?> newSubject(SubjectDto subjectDto) {

        if (subjectDto.getName() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return subjectService.addSubject(subjectDto);
    }

    /**
     * Method to get all data in a specific subject
     * @param id
     * @return ResponseEntity (ok: subject, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all subject data. Authentication required (USER)")
    public ResponseEntity<?> getSubject(@PathVariable Long id, @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates the subject
        if (!subjectRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return subjectService.getSubject(id, username);
    }

    /**
     * Method to get all subjects that the user has access to
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: Set(SubjectDtoResponse), no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/")
    @ApiOperation("Gets all subjects which the user has access to. Authentication required (USER)")
    public ResponseEntity<?> getSubjectsAllowed(@CurrentSecurityContext(expression="authentication?.name") String username){
        return subjectService.getSubjectsAllowed(username);
    }

    /**
     * Method to get all subjects
     * @return ResponseEntity (ok: Set(SubjectDtoResponse), no content)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all")
    @ApiOperation("Gets all subjects. Authentication required (ADMIN)")
    public ResponseEntity<?> getAllSubjects(){
        return subjectService.getAllSubjects();
    }

    /**
     * Method that updates the subject. ADMIN only
     * @param id
     * @param subjectDto
     * @return ResponseEntity (ok: Subject, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation("Updates subject data. Authentication required (ADMIN)")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, SubjectDto subjectDto){

        //Validates DTO
        if (subjectDto.getName() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates ID
        if (!subjectRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return subjectService.updateSubject(id, subjectDto);
    }

    /**
     * Method to delete subject. ADMIN only
     * @param id
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes subject. Authentication required (ADMIN)")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id){

        //Validates the id
        if (!subjectRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return subjectService.deleteSubject(id);
    }

}
