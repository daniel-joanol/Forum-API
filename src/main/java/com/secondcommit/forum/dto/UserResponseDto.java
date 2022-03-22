package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Post;

import java.util.Set;

/**
 * DTO with the response data sent after the creation of a new user
 */
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<BasicSubjectDtoResponse> getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Set<BasicSubjectDtoResponse> hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Set<BasicSubjectDtoResponse> getFollowsSubject() {
        return followsSubject;
    }

    public void setFollowsSubject(Set<BasicSubjectDtoResponse> followsSubject) {
        this.followsSubject = followsSubject;
    }

    public Set<Post> getFollowsPost() {
        return followsPost;
    }

    public void setFollowsPost(Set<Post> followsPost) {
        this.followsPost = followsPost;
    }
}
