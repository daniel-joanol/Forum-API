package com.secondcommit.forum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the required data for the creation or update of a Subject
 */
@Data
public class SubjectDto {

    private String name;
    private MultipartFile avatar;

    public SubjectDto() {
    }

    public SubjectDto(String name) {
        this.name = name;
    }

}
