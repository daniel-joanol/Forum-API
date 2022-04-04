package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * DTO with the required data for setting a new password for the user
 */
@Data
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

}
