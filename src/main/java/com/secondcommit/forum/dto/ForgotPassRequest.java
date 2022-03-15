package com.secondcommit.forum.dto;

/**
 * DTO with the required data for asking for a new password
 */
public class ForgotPassRequest {

    private String email;

    public ForgotPassRequest() {
    }

    public ForgotPassRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
