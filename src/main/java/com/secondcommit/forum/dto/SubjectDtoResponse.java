package com.secondcommit.forum.dto;

/**
 * Dto with the subject data send in responses
 */
public class SubjectDtoResponse {

    private Long id;
    private String name;

    public SubjectDtoResponse() {
    }

    public SubjectDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
