package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO with the response data sent after the creation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String username;
    private String avatar;
    private List<BasicSubjectDtoResponse> hasAccess;
    private boolean isActivated;
    private List<BasicSubjectDtoResponse> followsSubject;
    private List<Post> followsPost;
    private String message;
    
}
