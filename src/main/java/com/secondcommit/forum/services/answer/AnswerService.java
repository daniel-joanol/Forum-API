package com.secondcommit.forum.services.answer;

import com.secondcommit.forum.dto.AnswerDto;
import com.secondcommit.forum.entities.Answer;
import com.secondcommit.forum.entities.Post;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Answer Service Interface
 */
public interface AnswerService {

    ResponseEntity<?> addAnswer(Long post_id, AnswerDto answerDto, String author);
    ResponseEntity<?> getAnswer(Long id);
    ResponseEntity<?> updateAnswer(Long id, AnswerDto answerDto, String username);
    ResponseEntity<?> deleteAnswer(Long id, String username);
    void deleteAnswer(Post post);
    ResponseEntity<?> like(Long id, String username);
    ResponseEntity<?> dislike(Long id, String username);
    ResponseEntity<?> fix(Long id);
}
