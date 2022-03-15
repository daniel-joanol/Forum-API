package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.entities.User;
import org.springframework.http.ResponseEntity;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(NewUserRequest newUser);
    ResponseEntity<?> activateUser(User user, Integer activationCode);
}
