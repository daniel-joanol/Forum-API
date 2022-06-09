package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * DTO with the required data for the creation of a new user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    private String email;
    private String username;
    private String password;
    private Set<String> hasAccess;
    private MultipartFile avatar;

}
