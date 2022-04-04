package com.secondcommit.forum.dto;

import lombok.Data;

/**
 * Dto with the required data for the creation and update of a module
 */
@Data
public class ModuleDto {

    private String name;
    private String description;

    public ModuleDto() {
    }

    public ModuleDto(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
