package com.secondcommit.forum.services.post;

import com.secondcommit.forum.dto.PostDto;
import org.springframework.http.ResponseEntity;

/**
 * Post service interface
 */
public interface PostService {

    ResponseEntity<?> addPost(Long id, PostDto postDto, String author);
    ResponseEntity<?> getPost(Long id, String username);
    ResponseEntity<?> updatePost(Long id, PostDto postDto, String username);
    ResponseEntity<?> deletePost(Long id, String username);
    ResponseEntity<?> like(Long id, String username);
    ResponseEntity<?> dislike(Long id, String username);
    ResponseEntity<?> fix(Long id);
    ResponseEntity<?> follow(Long id, String username);
    ResponseEntity<?> unfollow(Long id, String username);
}
