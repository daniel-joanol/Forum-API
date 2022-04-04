package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.ModuleDtoResponse;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity that manages the subjects in the database
 */
@Data
@Entity
@Table(name = "modules")
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "total_questions")
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
