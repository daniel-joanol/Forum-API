package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * Dto with the basic information for users in some endpoints
 */
@Data
public class BasicUserDto {

    private Long id;
    private String username;

    public BasicUserDto() {
    }

    public BasicUserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
