package com.secondcommit.forum.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the data required to create and update posts
 */
public class PostDto {

    private String title;
    private String content;
    private MultipartFile[] files;

    public PostDto() {
    }

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
