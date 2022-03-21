package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Module;
import java.util.Set;

/**
 * Dto with the required data for the creation or update of a Subject
 */
public class SubjectDto {

    private String name;

    public SubjectDto() {
    }

    public SubjectDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
