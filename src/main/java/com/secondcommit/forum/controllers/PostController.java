package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.PostDto;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.post.PostServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to manage the Post CRUD methods
 */
@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostServiceImpl postService;

    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    /**
     * Method to create new post
     * @param postDto (title and content)
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/")
    @ApiOperation("Creates new post")
    public ResponseEntity<?> newPost(@RequestBody PostDto postDto, @CurrentSecurityContext(expression="authentication?.name") String username) {

        //Validates post
        if (postDto.getContent() == null || postDto.getTitle() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return postService.addPost(postDto, username);
    }

    /**
     * Method to get the post
     * @param id
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets POST")
    public ResponseEntity<?> getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    @ApiOperation("Updates the post")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDto postDto,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates Dto
        if (postDto.getTitle() == null && postDto.getContent() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing"));

        return postService.updatePost(id, postDto, username);
    }

    /**
     * Method to delete post
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes the post")
    public ResponseEntity<?> deletePost(Long id,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){
        return postService.deletePost(id, username);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/like/{id}")
    @ApiOperation("Adds or removes like, depending on the previous state")
    public ResponseEntity<?> like(Long id,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){
        return postService.like(id, username);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/dislike/{id}")
    @ApiOperation("Adds or removes dislike, depending on the previous state")
    public ResponseEntity<?> dislike(Long id,
                                  @CurrentSecurityContext(expression="authentication?.name") String username){
        return postService.dislike(id, username);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/fix/{id}")
    @ApiOperation("Fix post. Only admins are allowed")
    public ResponseEntity<?> fix(Long id){
        return postService.fix(id);
    }
}