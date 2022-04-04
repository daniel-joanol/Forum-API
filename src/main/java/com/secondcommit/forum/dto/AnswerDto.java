package com.secondcommit.forum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the data required to create and update answers
 */
@Data
public class AnswerDto {

    private String content;
    private MultipartFile[] files;

    public AnswerDto() {
    }

    public AnswerDto(String content) {
        this.content = content;
    }

}
