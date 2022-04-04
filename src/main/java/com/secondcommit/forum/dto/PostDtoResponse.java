package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Answer;
import lombok.Data;

import java.util.Date;
import java.util.Set;

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
    private Set<Answer> answers;
    private Integer totalAnswers;
    private Set<String> files;
    private Set<BasicUserDto> usersWhoLike;
    private Set<BasicUserDto> usersWhoDisike;

    public PostDtoResponse() {
    }

    public PostDtoResponse(Long id, String title, String content, String author, Integer totalLikes, Integer totalDislikes,
                           Date date, Boolean fixed, Set<Answer> answers, Integer totalAnswers, Set<String> files,
                           Set<BasicUserDto> usersWhoLike, Set<BasicUserDto> usersWhoDisike) {
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
