package com.secondcommit.forum.services.authentication;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.entities.User;
import org.springframework.http.ResponseEntity;

/**
 * Authentication service interface
 */
public interface AuthService {

    ResponseEntity<?> askNewPass(User user);
    ResponseEntity<?> setNewPass(User user, String newPass, Integer validationCode);

}
