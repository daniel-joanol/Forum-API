package com.secondcommit.forum.dto;

/**
 * Dto with the basic information for users in some endpoints
 */
public class BasicUserDto {

    private Long id;
    private String username;

    public BasicUserDto() {
    }

    public BasicUserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
