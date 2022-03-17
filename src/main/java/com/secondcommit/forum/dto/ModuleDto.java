package com.secondcommit.forum.dto;

/**
 * Dto with the required data for the creation and update of a module
 */
public class ModuleDto {

    private String name;
    private String description;

    public ModuleDto() {
    }

    public ModuleDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
