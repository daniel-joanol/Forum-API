package com.secondcommit.forum.dto;

import com.secondcommit.forum.entities.Module;
import java.util.Set;

/**
 * Dto with the required data for the creation or update of a Subject
 */
public class SubjectDto {

    private String name;
    private Set<Module> modules;

    public SubjectDto() {
    }

    public SubjectDto(String name, Set<Module> modules) {
        this.name = name;
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }
}
