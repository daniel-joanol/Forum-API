package com.secondcommit.forum.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * Dto with the required data for the creation or update of a Subject
 */
public class SubjectDto {

    private String name;
    private Set<ModuleDto> modules;
    private MultipartFile avatar;

    public SubjectDto() {
    }

    public SubjectDto(String name, Set<ModuleDto> modules) {
        this.name = name;
        this.modules = modules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ModuleDto> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleDto> modules) {
        this.modules = modules;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
