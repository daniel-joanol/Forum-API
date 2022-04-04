package com.secondcommit.forum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the data required to create and update posts
 */
@Data
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

}
