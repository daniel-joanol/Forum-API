package com.secondcommit.forum.services.subject;

import com.secondcommit.forum.dto.SubjectDto;
import com.secondcommit.forum.entities.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * Subject service interface
 */
public interface SubjectService {

    ResponseEntity<?> addSubject(SubjectDto subjectDto);
    ResponseEntity<?> addAvatarToSubject(Subject subject, MultipartFile avatar);
    ResponseEntity<?> getSubject(Long id, String username);
    ResponseEntity<?> getSubjectsAllowed(String username);
    ResponseEntity<?> updateSubject(Long id, SubjectDto subjectDto);
    ResponseEntity<?> deleteSubject(Long id);
}
