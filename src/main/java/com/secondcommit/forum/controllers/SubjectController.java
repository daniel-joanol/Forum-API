package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.SubjectDto;
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

    public SubjectController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * Creates a new subject from subjectDto
     * @param subjectDto
     * @return ResponseEntity<Subject>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    @ApiOperation("Creates new subject")
    public ResponseEntity<?> newSubject(@RequestBody SubjectDto subjectDto) {

        if (subjectDto.getName() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return subjectService.addSubject(subjectDto);
    }

    /**
     * Method to get a specific subject
     * @param id
     * @return ResponseEntity (ok: subject, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all subject data")
    public ResponseEntity<?> getSubject(@PathVariable Long id){
        return subjectService.getSubject(id);
    }

    /**
     * Method to get all subjects that the user has access to
     * @param username
     * @return ResponseEntity (ok: Set(SubjectDtoResponse), no content)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/")
    @ApiOperation("Gets all subject data")
    public ResponseEntity<?> getSubjectsAllowed(@CurrentSecurityContext(expression="authentication?.name") String username){
        return subjectService.getSubjectsAllowed(username);
    }

    /**
     * Method that updates the subject
     * @param id
     * @param subjectDto
     * @return ResponseEntity<Subject>
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation("Updates subject data")
    public ResponseEntity<?> updateSubject(@PathVariable Long id, @RequestBody SubjectDto subjectDto){

        if (subjectDto.getName() == null && subjectDto.getModules() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return subjectService.updateSubject(id, subjectDto);
    }

    /**
     * Deletes subject
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes subject")
    public ResponseEntity<?> deleteSubject(@PathVariable Long id){
        return subjectService.deleteSubject(id);
    }

    //TODO: Create DTO for the module and update the subject response dto. It's sending too much information back
}
