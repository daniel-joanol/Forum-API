package com.secondcommit.forum.dto;

/**
 * DTO with the required data for the activation of a new user
 */
public class ActivateUserRequest {

    private String username;
    private Integer activationCode;

    public ActivateUserRequest() {
    }

    public ActivateUserRequest(String username, Integer activationCode) {
        this.username = username;
        this.activationCode = activationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(Integer activationCode) {
        this.activationCode = activationCode;
    }
}
