package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.PostDto;
import com.secondcommit.forum.repositories.PostRepository;
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
    private final PostRepository postRepository;

    public PostController(PostServiceImpl postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    /**
     * Method to create new post
     * @param postDto (title and content)
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/")
    @ApiOperation("Creates new post. Authentication required (USER)")
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
    @ApiOperation("Gets post. Authentication required (USER)")
    public ResponseEntity<?> getPost(@PathVariable Long id){

        //Validates post
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.getPost(id);
    }

    /**
     * Method to update post.
     * @param id
     * @param postDto
     * @param username (Gets from the jwt token)
     * @return ResponseEntity (ok: post, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    @ApiOperation("Updates the post. Authentication required (USER)")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody PostDto postDto,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates Dto
        if (postDto.getTitle() == null && postDto.getContent() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing"));

        //Validates id
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.updatePost(id, postDto, username);
    }

    /**
     * Method to delete post
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes the post. Authentication required (USER)")
    public ResponseEntity<?> deletePost(Long id,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.deletePost(id, username);
    }

    /**
     * Method to add or remove like from post
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity(ok: totalLikes, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/like/{id}")
    @ApiOperation("Adds or removes like, depending on the previous state. Authentication required (USER)")
    public ResponseEntity<?> like(Long id,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.like(id, username);
    }

    /**
     * Method to add or remove dislike from post
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity(ok: totalDislikes, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/dislike/{id}")
    @ApiOperation("Adds or removes dislike, depending on the previous state. Authentication required (USER)")
    public ResponseEntity<?> dislike(Long id,
                                  @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.dislike(id, username);
    }

    /**
     * Method to fix or unfix a post. ADMIN only
     * @param id
     * @return ResponseEntity (MessageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/fix/{id}")
    @ApiOperation("Fix post. . Authentication required (ADMIN)")
    public ResponseEntity<?> fix(Long id){

        //Validates id
        if (!postRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return postService.fix(id);
    }
}
