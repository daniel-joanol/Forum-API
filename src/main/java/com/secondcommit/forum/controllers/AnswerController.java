package com.secondcommit.forum.controllers;

import com.secondcommit.forum.dto.AnswerDto;
import com.secondcommit.forum.repositories.AnswerRepository;
import com.secondcommit.forum.repositories.PostRepository;
import com.secondcommit.forum.security.payload.MessageResponse;
import com.secondcommit.forum.services.answer.AnswerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to manage the Answer CRUD methods
 */
@RestController
@RequestMapping("/api/post/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final AnswerRepository answerRepository;
    private final PostRepository postRepository;

    public AnswerController(AnswerService answerService, AnswerRepository answerRepository, PostRepository postRepository) {
        this.answerService = answerService;
        this.answerRepository = answerRepository;
        this.postRepository = postRepository;
    }

    /**
     * Method to create new answer
     * @param postId
     * @param answerDto (content)
     * @param username (gets from the jwt token)
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/{postId}")
    @ApiOperation("Creates new answer. Authentication required (USER)")
    public ResponseEntity<?> newAnswer(@RequestParam Long postId,
                                     @RequestBody AnswerDto answerDto,
                                     @CurrentSecurityContext(expression="authentication?.name") String username) {

        //Validates id
        if (!postRepository.existsById(postId))
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("The post + " + postId + " doesn't exist"));

        //Validates dto
        if (answerDto.getContent() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        return answerService.addAnswer(postId, answerDto, username);
    }

    /**
     * Method to get the answer
     * @param id
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{id}")
    @ApiOperation("Gets answer. Authentication required (USER)")
    public ResponseEntity<?> getAnswer(@PathVariable Long id){

        //Validates answer
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.getAnswer(id);
    }

    /**
     * Method to update answer.
     * @param id
     * @param answerDto (content)
     * @param username (Gets from the jwt token)
     * @return ResponseEntity (ok: answer, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/{id}")
    @ApiOperation("Updates the answer. Authentication required (USER)")
    public ResponseEntity<?> updateAnswer(@PathVariable Long id,
                                        @RequestBody AnswerDto answerDto,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates Dto
        if (answerDto.getContent() == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Missing parameters"));

        //Validates id
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.updateAnswer(id, answerDto, username);
    }

    /**
     * Method to delete answer
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity (messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    @ApiOperation("Deletes the answer. Authentication required (USER)")
    public ResponseEntity<?> deleteAnswer(@RequestParam Long id,
                                        @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.deleteAnswer(id, username);
    }

    /**
     * Method to add or remove like from answer
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity(ok: totalLikes, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/like/{id}")
    @ApiOperation("Adds or removes like, depending on the previous state. Authentication required (USER)")
    public ResponseEntity<?> like(@RequestParam Long id,
                                  @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.like(id, username);
    }

    /**
     * Method to add or remove dislike from answer
     * @param id
     * @param username (Gets from the jwt token)
     * @return ResponseEntity(ok: totalDislikes, bad request: messageResponse)
     */
    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/dislike/{id}")
    @ApiOperation("Adds or removes dislike, depending on the previous state. Authentication required (USER)")
    public ResponseEntity<?> dislike(@RequestParam Long id,
                                     @CurrentSecurityContext(expression="authentication?.name") String username){

        //Validates id
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.dislike(id, username);
    }

    /**
     * Method to fix or unfix a answer. ADMIN only
     * @param id
     * @return ResponseEntity (MessageResponse)
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/fix/{id}")
    @ApiOperation("Fix answer. Authentication required (ADMIN)")
    public ResponseEntity<?> fix(@RequestParam Long id){

        //Validates id
        if (!answerRepository.existsById(id))
            return ResponseEntity.badRequest().body(new MessageResponse("Wrong id"));

        return answerService.fix(id);
    }
}
