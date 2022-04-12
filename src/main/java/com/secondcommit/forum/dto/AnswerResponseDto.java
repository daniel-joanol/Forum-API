package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * DTO with the basic data of an Answer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

}
