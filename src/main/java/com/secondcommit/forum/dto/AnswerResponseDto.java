package com.secondcommit.forum.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * DTO with the basic data of an Answer
 */
@Data
public class AnswerResponseDto {

    private Long id;
    private String content;
    private String author;
    private List<String> files;
    private Date date;
    private Boolean fixed;
    private Integer totalLikes;
    private Integer totalDislikes;
    private List<String> usersWhoLike;
    private List<String> usersWhoDislike;

    public AnswerResponseDto(Long id, String content, String author, List<String> files, Date date, Boolean fixed,
                             Integer totalLikes, Integer totalDislikes, List<String> usersWhoLike,
                             List<String> usersWhoDislike) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.files = files;
        this.date = date;
        this.fixed = fixed;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.usersWhoLike = usersWhoLike;
        this.usersWhoDislike = usersWhoDislike;
    }
}
