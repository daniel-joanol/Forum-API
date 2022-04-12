package com.secondcommit.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto with the response sent for any CRUD methods involving Module
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuleDtoResponse {

    private Long id;
    private String name;
    private String description;
    private Integer totalQuestions;

}
