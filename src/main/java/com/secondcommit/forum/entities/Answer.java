package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.AnswerResponseDto;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * Entity that manages the answer in the database
 */
@Data
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1048, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "POST_FILES",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private List<File> files = new ArrayList<>();

    private Date date = new Date();

    private Boolean fixed = false;

    @Column(name = "total_likes")
    private Integer totalLikes = 0;

    @Column(name = "total_dislikes")
    private Integer totalDislikes = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_WHOLIKE_POST",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID") })
    private List<User> usersWhoLike = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_WHODISLIKE_POST",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID") })
    private List<User> usersWhoDislike = new ArrayList<>();

    @ManyToOne()
    private Post post;

    //Constructors
    public Answer() {
    }

    public Answer(String content, User author, Post post) {
        this.content = content;
        this.author = author;
        date = new Date();
        this.post = post;
    }

    public Answer(String content, User author, List<File> files, Post post) {
        this.content = content;
        this.author = author;
        this.files = files;
        date = new Date();
        this.post = post;
    }

    public void refreshLikes(){
        totalLikes = usersWhoLike.size();
        totalDislikes = usersWhoDislike.size();
    }

    public AnswerResponseDto getDtoFromAnswer(){

        List<String> fileAdressess = new ArrayList<>();
        for (File file: files) fileAdressess.add(file.getUrl());

        List<String> usernamesWhoLike = new ArrayList<>();
        for (User user: usersWhoLike) usernamesWhoLike.add(user.getUsername());

        List<String> usernamesWhoDislike = new ArrayList<>();
        for (User user: usersWhoDislike) usernamesWhoDislike.add(user.getUsername());

        return new AnswerResponseDto(id, content, author.getUsername(), fileAdressess, date, fixed, totalLikes,
                totalDislikes, usernamesWhoLike, usernamesWhoDislike);

    }

}
