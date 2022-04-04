package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * DTO with the required data for asking for a new password
 */
@Data
public class ForgotPassRequest {

    private String email;

    public ForgotPassRequest() {
    }

    public ForgotPassRequest(String email) {
        this.email = email;
    }
}
