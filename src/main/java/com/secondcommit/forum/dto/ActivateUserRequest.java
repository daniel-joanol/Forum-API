package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * DTO with the required data for the activation of a new user
 */
@Data
public class ActivateUserRequest {

    private String username;
    private Integer activationCode;

    public ActivateUserRequest() {
    }

    public ActivateUserRequest(String username, Integer activationCode) {
        this.username = username;
        this.activationCode = activationCode;
    }
}
