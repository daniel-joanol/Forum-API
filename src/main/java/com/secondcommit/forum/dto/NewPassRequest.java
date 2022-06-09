package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO with the required data for setting a new password for the user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPassRequest {

    private String username;
    private String newPass;
    private Integer validationCode;

}
