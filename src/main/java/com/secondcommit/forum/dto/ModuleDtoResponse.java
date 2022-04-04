package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * Dto with the response sent for any CRUD methods involving Module
 */
@Data
public class ModuleDtoResponse {

    private Long id;
    private String name;
    private String description;
    private Integer totalQuestions;

    public ModuleDtoResponse() {
    }

    public ModuleDtoResponse(Long id, String name, String description, Integer totalQuestions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalQuestions = totalQuestions;
    }

}
