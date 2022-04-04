package com.secondcommit.forum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * DTO with the required data for the creation of a new user
 */
@Data
public class NewUserRequest {

    private String email;
    private String username;
    private String password;
    private Set<String> hasAccess;
    private MultipartFile avatar;

    public NewUserRequest() {
    }

    public NewUserRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public NewUserRequest(String email, String username, String password, Set<String> hasAccess) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.hasAccess = hasAccess;
    }

}
