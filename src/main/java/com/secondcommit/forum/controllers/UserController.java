package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.dto.UpdateUserDto;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.user.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 *  Controller to manage the User CRUD
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    public UserController(UserRepository userRepository, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Gets the user data
     * @param id
     * @return ResponseEntity (ok: user, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all user data")
    public ResponseEntity<?> getUser(@PathVariable Long id){

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.getUser(id);
    }

    /**
     * Updates user (only username, email and isValidated)
     * @param id
     * @param userDto
     * @return ReponseEntity (with the User or an error message)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    @ApiOperation("Updates user")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates the DTO
        if (userDto.getUsername() == null || userDto.getEmail() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.updateUser(id, userDto, username);
    }

    /**
     * Removes user
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes user")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @CurrentSecurityContext(expression="authentication?.name") String username){
        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.deleteUser(id, username);
    }

    /**
     * Method to add access to a subject. Only admin is allowed
     * @param id
     * @param subjectDto
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/add-access/{id}")
    @ApiOperation("Add access to subject")
    public ResponseEntity<?> addAccess(@PathVariable Long id, @RequestBody SubjectDto subjectDto){
        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.addAccess(id, subjectDto);
    }

    /**
     * Method to remove access from a subject. Only admin is allowed
     * @param id
     * @param subjectDto
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/remove-access/{id}")
    @ApiOperation("Remove to subject")
    public ResponseEntity<?> removeAccess(@PathVariable Long id, @RequestBody SubjectDto subjectDto){
        //Validates the id
        if (!userRepository.existsById(id))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The user id " + id + " doesn't exist!"));

        return userService.removeAccess(id, subjectDto);
    }
}
