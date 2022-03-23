package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Answer;

import java.util.Date;
import java.util.Set;

/**
 * Dto with the post data for responses
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(Integer totalLikes) {
        this.totalLikes = totalLikes;
    }

    public Integer getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(Integer totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public Integer getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(Integer totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public Set<String> getFiles() {
        return files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
    }

    public Set<BasicUserDto> getUsersWhoLike() {
        return usersWhoLike;
    }

    public void setUsersWhoLike(Set<BasicUserDto> usersWhoLike) {
        this.usersWhoLike = usersWhoLike;
    }

    public Set<BasicUserDto> getUsersWhoDisike() {
        return usersWhoDisike;
    }

    public void setUsersWhoDisike(Set<BasicUserDto> usersWhoDisike) {
        this.usersWhoDisike = usersWhoDisike;
    }
}
