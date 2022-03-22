package com.secondcommit.forum.dto;

import java.util.Set;

/**
 * Dto with the subject data send in responses
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Set<ModuleDtoResponse> getModules() {
        return modules;
    }

    public void setModules(Set<ModuleDtoResponse> modules) {
        this.modules = modules;
    }
}
