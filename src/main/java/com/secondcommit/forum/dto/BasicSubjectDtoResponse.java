package com.secondcommit.forum.dto;

/**
 * Dto with the basic data of the subject for some responses, when the data from the module and avatar are not needed
 */
public class BasicSubjectDtoResponse {

    private Long id;
    private String name;

    public BasicSubjectDtoResponse() {
    }

    public BasicSubjectDtoResponse(Long id, String name) {
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
