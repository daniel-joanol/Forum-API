package com.secondcommit.forum.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the data required to create and update answers
 */
public class AnswerDto {

    private String content;
    private MultipartFile[] files;

    public AnswerDto() {
    }

    public AnswerDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }
}
