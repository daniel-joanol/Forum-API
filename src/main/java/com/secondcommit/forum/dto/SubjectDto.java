package com.secondcommit.forum.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Dto with the required data for the creation or update of a Subject
 */
public class SubjectDto {

    private String name;
    private MultipartFile avatar;

    public SubjectDto() {
    }

    public SubjectDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
