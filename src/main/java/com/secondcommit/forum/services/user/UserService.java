package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.dto.UpdateUserDto;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(NewUserRequest newUser);
    ResponseEntity<?> activateUser(User user, Integer activationCode);
    ResponseEntity<?> addAvatar(String username, MultipartFile avatar);
    ResponseEntity<?> getUser(Long id);
    ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto);
    ResponseEntity<?> deleteUser(Long id);
    ResponseEntity<?> addAccess(Long id, Subject subject, String username);
    ResponseEntity<?> removeAccess(Long id, Subject subject, String username);
}
