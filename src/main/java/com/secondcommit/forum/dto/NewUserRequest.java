package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.File;
import com.secondcommit.forum.entities.Subject;

import java.util.Set;

/**
 * DTO with the required data for the creation of a new user
 */
public class NewUserRequest {

    private String email;
    private String username;
    private String password;
    private Boolean rememberMe;
    private File avatar;
    private Set<Subject> hasAccess;

    public NewUserRequest() {
    }

    public NewUserRequest(String email, String username, String password, Boolean rememberMe, File avatar, Set<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.rememberMe = rememberMe;
        this.avatar = avatar;
        this.hasAccess = hasAccess;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
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
}
