package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.ActivateUserRequest;
import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.dto.UpdateUserDto;
import com.secondcommit.forum.entities.User;
import com.secondcommit.forum.repositories.UserRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.user.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
     * @return ResponseEntity (with the User or an error message)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets all user data")
    public ResponseEntity<?> getUser(@PathVariable Long id){
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
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto userDto){

        if (userDto.getUsername() == null || userDto.getEmail() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return userService.updateUser(id, userDto);
    }

    /**
     * Removes user
     * @param id
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes user")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
