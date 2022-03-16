package com.secondcommit.forum.services.subject;

import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Subject;
import com.secondcommit.forum.repositories.SubjectRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.cloudinary.CloudinaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Subject service interface
 */
public interface SubjectService {

    ResponseEntity<?> addSubject(Subject subject);
    ResponseEntity<?> addAvatarToSubject(Subject subject, MultipartFile avatar);

    /**
     * Implementation of the Subject Service Interface
     */
    @Service
    class SubjectServiceImpl implements SubjectService {

        @Autowired
        private SubjectRepository subjectRepository;

        @Autowired
        private CloudinaryServiceImpl cloudinary;

        public SubjectServiceImpl(SubjectRepository subjectRepository, CloudinaryServiceImpl cloudinary) {
            this.subjectRepository = subjectRepository;
            this.cloudinary = cloudinary;
        }

        @Override
        public ResponseEntity<?> addSubject(Subject subject) {
            subjectRepository.save(subject);

            return ResponseEntity.ok().build();
        }

        @Override
        public ResponseEntity<?> addAvatarToSubject(Subject subject, MultipartFile avatar) {

            try {
                File file = new File(cloudinary.uploadImage(avatar));
                subject.setAvatar(file);
                subjectRepository.save(subject);

            } catch (Exception e){
                System.out.println("Error : " + e.getMessage());
                return ResponseEntity.badRequest().body(new MessageResponse("Failed to upload avatar"));
            }

            return ResponseEntity.ok(subject.getAvatar().getUrl());
        }
    }
}
