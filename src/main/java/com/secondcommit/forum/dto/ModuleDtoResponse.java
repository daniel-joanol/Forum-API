package com.secondcommit.forum.dto;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}
