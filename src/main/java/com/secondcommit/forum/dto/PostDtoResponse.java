package com.secondcommit.forum.dto;

import java.util.Date;

/**
 * Dto with the post data for responses
 */
public class PostDtoResponse {

    private Long id;
    private String title;
    private String content;
    private Integer totalLikes;
    private Integer totalDislikes;
    private Date date;
    private Boolean fixed;
}
