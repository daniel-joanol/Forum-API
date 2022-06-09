package com.secondcommit.forum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * Dto with the data required to create and update posts
 */
@Data
@NoArgsConstructor
public class PostDto {

    private String title;
    private String content;
    private MultipartFile[] files;

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
