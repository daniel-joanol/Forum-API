package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.ModuleDtoResponse;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity that manages the subjects in the database
 */
@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer totalQuestions = 0;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "MODULE_POSTS",
            joinColumns = {
                    @JoinColumn(name = "MODULE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "POST_ID") })
    private Set<Post> posts;

    public Module() {
    }

    public Module(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Module(String name, String description, Set<Post> posts) {
        this.name = name;
        this.description = description;
        this.posts = posts;
        totalQuestions = posts.size();
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

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public void addPost(Post post){
        posts.add(post);
        totalQuestions = posts.size();
    }

    public void removePost(Post post){
        posts.remove(post);
        totalQuestions = posts.size();
    }

    public ModuleDtoResponse getDtoFromModule(){
        return new ModuleDtoResponse(id, name, description, totalQuestions);
    }
}
