package com.secondcommit.forum.dto;

/**
 * DTO with the required data for setting a new password for the user
 */
public class NewPassRequest {

    private String username;
    private String newPass;
    private Integer validationCode;

    public NewPassRequest() {
    }

    public NewPassRequest(String username, String newPass, Integer validationCode) {
        this.username = username;
        this.newPass = newPass;
        this.validationCode = validationCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public Integer getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(Integer validationCode) {
        this.validationCode = validationCode;
    }
}
