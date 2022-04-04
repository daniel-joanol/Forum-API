package com.secondcommit.forum.dto;

import lombok.Data;

import java.util.Set;

/**
 * Dto with the subject data send in responses
 */
@Data
public class SubjectDtoResponse {

    private Long id;
    private String name;
    private String avatar;
    private Set<ModuleDtoResponse> modules;

    public SubjectDtoResponse() {
    }

    public SubjectDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubjectDtoResponse(Long id, String name, String avatar, Set<ModuleDtoResponse> modules) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.modules = modules;
    }

}
