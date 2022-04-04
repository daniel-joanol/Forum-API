package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Post;
import lombok.Data;

import java.util.Set;

/**
 * DTO with the response data sent after the creation of a new user
 */
@Data
public class UserResponseDto {

    private String message;
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private Set<BasicSubjectDtoResponse> hasAccess;
    private boolean isActivated;
    private Set<BasicSubjectDtoResponse> followsSubject;
    private Set<Post> followsPost;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String email, String username, String avatar, Set<BasicSubjectDtoResponse> hasAccess,
                           boolean isActivated, Set<BasicSubjectDtoResponse> followsSubject, Set<Post> followsPost, String message) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.hasAccess = hasAccess;
        this.isActivated = isActivated;
        this.followsSubject = followsSubject;
        this.followsPost = followsPost;
        this.message = message;
    }

}
