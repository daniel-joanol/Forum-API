package com.secondcommit.forum.dto;

import lombok.Data;

import java.util.Set;

/**
 * DTO with the required data to update User
 */
@Data
public class UpdateUserDto {

    private String email;
    private String username;
    private Boolean isActivated;
    private Set<String> hasAccess;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String email, String username, Boolean isActivated, Set<String> hasAccess) {
        this.email = email;
        this.username = username;
        this.isActivated = isActivated;
        this.hasAccess = hasAccess;
    }

}
