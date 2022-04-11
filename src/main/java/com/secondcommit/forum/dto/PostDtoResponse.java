package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Answer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Dto with the post data for responses
 */
@Data
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

    public PostDtoResponse() {
    }

    public PostDtoResponse(Long id, String title, String content, String author, Integer totalLikes, Integer totalDislikes,
                           Date date, Boolean fixed, List<Answer> answers, Integer totalAnswers, List<String> files,
                           List<BasicUserDto> usersWhoLike, List<BasicUserDto> usersWhoDisike) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.totalLikes = totalLikes;
        this.totalDislikes = totalDislikes;
        this.date = date;
        this.fixed = fixed;
        this.answers = answers;
        this.totalAnswers = totalAnswers;
        this.files = files;
        this.usersWhoLike = usersWhoLike;
        this.usersWhoDisike = usersWhoDisike;
    }

}
