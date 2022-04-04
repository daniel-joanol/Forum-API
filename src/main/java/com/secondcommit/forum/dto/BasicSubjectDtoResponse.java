package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * Dto with the basic data of the subject for some responses, when the data from the module and avatar are not needed
 */
@Data
public class BasicSubjectDtoResponse {

    private Long id;
    private String name;

    public BasicSubjectDtoResponse() {
    }

    public BasicSubjectDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
