package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Dto with the post data for responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Integer totalLikes;
    private Integer totalDislikes;
    private Date date;
    private Boolean fixed;
    private List<Answer> answers;
    private Integer totalAnswers;
    private List<String> files;
    private List<BasicUserDto> usersWhoLike;
    private List<BasicUserDto> usersWhoDisike;

}
