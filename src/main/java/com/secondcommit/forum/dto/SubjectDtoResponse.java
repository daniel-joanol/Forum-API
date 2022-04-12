package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Dto with the subject data send in responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDtoResponse {

    private Long id;
    private String name;
    private String avatar;
    private Set<ModuleDtoResponse> modules;

    public SubjectDtoResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
