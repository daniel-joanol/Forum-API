package com.secondcommit.forum.dto;

/**
 * Dto with the data required to create and update answers
 */
public class AnswerDto {

    private String content;

    public AnswerDto() {
    }

    public AnswerDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
