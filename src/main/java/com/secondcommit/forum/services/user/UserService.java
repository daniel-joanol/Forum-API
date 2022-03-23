package com.secondcommit.forum.services.user;

import com.secondcommit.forum.dto.NewUserRequest;
import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.dto.UpdateUserDto;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * User service interface
 */
public interface UserService {

    ResponseEntity<?> createUser(NewUserRequest newUser, String roleName);
    ResponseEntity<?> activateUser(User user, Integer activationCode);
    ResponseEntity<?> addAvatar(String username, MultipartFile avatar);
    ResponseEntity<?> getUser(Long id);
    ResponseEntity<?> updateUser(Long id, UpdateUserDto userDto, String username);
    ResponseEntity<?> deleteUser(Long id, String username);
    ResponseEntity<?> addAccess(Long id, SubjectDto subjectDto);
    ResponseEntity<?> removeAccess(Long id, SubjectDto subjectDto);
}
