package com.secondcommit.forum.dto;

/**
 * DTO with the required data to update User
 */
public class UpdateUserDto {

    private String email;
    private String username;
    private Boolean isActivated;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String email, String username, Boolean isActivated) {
        this.email = email;
        this.username = username;
        this.isActivated = isActivated;
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
}
