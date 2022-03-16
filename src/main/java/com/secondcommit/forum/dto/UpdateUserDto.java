package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Subject;

import java.util.Set;

/**
 * DTO with the required data to update User
 */
public class UpdateUserDto {

    private String email;
    private String username;
    private Boolean isActivated;
    private Set<Subject> hasAccess;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String email, String username, Boolean isActivated, Set<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.isActivated = isActivated;
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

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Set<Subject> getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Set<Subject> hasAccess) {
        this.hasAccess = hasAccess;
    }
}
