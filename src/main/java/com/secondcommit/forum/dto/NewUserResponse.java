package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Post;
import com.secondcommit.forum.entities.Subject;

import java.util.Set;

/**
 * DTO with the response data sent after the creation of a new user
 */
public class NewUserResponse {

    private Long id;
    private String email;
    private String username;
    private Boolean rememberMe;
    private File avatar;
    private Set<Subject> hasAccess;
    private boolean isActivated;
    private Set<Subject> followsSubject;
    private Set<Post> followsPost;

    public NewUserResponse() {
    }

    public NewUserResponse(Long id, String email, String username, Boolean rememberMe, File avatar, Set<Subject> hasAccess,
                           boolean isActivated, Set<Subject> followsSubject, Set<Post> followsPost) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.rememberMe = rememberMe;
        this.avatar = avatar;
        this.hasAccess = hasAccess;
        this.isActivated = isActivated;
        this.followsSubject = followsSubject;
        this.followsPost = followsPost;
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

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    public Set<Subject> getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Set<Subject> hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Set<Subject> getFollowsSubject() {
        return followsSubject;
    }

    public void setFollowsSubject(Set<Subject> followsSubject) {
        this.followsSubject = followsSubject;
    }

    public Set<Post> getFollowsPost() {
        return followsPost;
    }

    public void setFollowsPost(Set<Post> followsPost) {
        this.followsPost = followsPost;
    }
}
