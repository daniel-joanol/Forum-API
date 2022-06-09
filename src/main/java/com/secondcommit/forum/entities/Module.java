package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.ModuleDtoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Entity that manages the subjects in the database
 */
@Data
@NoArgsConstructor
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "MODULE_POSTS",
            joinColumns = {
                    @JoinColumn(name = "MODULE_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "POST_ID") })
    private List<Post> posts;

    @ManyToOne
    private Subject subject;

    public Module(String name, String description, Subject subject) {
        this.name = name;
        this.description = description;
        this.subject = subject;
    }

    public Module(String name, String description, List<Post> posts, Subject subject) {
        this.name = name;
        this.description = description;
        this.posts = posts;
        totalQuestions = posts.size();
        this.subject = subject;
    }

    public ModuleDtoResponse getDtoFromModule(){
        return new ModuleDtoResponse(id, name, description, totalQuestions);
    }

    public void refreshTotalQuestions(){
        totalQuestions = posts.size();
    }
}
