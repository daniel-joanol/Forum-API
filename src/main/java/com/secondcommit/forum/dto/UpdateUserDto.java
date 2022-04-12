package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO with the required data to update User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String email;
    private String username;
    private Boolean isActivated;
    private Set<String> hasAccess;

}
