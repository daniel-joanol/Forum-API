package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.BasicUserDto;
import com.secondcommit.forum.dto.PostDtoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

/**
 * Entity that manages the posts in the database
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(length = 1048, nullable = false)
    private String content;

    @Column(name = "total_likes")
    private Integer totalLikes = 0;

    @Column(name = "total_dislikes")
    private Integer totalDislikes = 0;

    private Date date = new Date();

    private Boolean fixed = false;

    @ManyToMany(mappedBy ="followsPost")
    private List<User> usersFollowing = new ArrayList<>();

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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "POST_FILES",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private List<File> files = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "POST_ANSWERS",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ANSWERS_ID") })
    private List<Answer> answers = new ArrayList<>();

    @Column
    private Integer totalAnswers = 0;

    @ManyToOne
    private Module module;

    //Constructors
    public Post(User author, String title, String content, Module module) {
        this.author = author;
        this.title = title;
        this.content = content;
        date = new Date();
        this.module = module;
    }

    public Post(User author, String title, String content, List<File> files, Module module) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.files = files;
        date = new Date();
        this.module = module;
    }

    public PostDtoResponse getDtoFromPost(){

        List<String> filesUrl = new ArrayList<>();
        if (files != null){
            for (File file : files){
                filesUrl.add(file.getUrl());
            }
        }

        List<BasicUserDto> basicUsersWhoLike = new ArrayList<>();
        if (usersWhoLike != null){
            for (User user : usersWhoLike){
                basicUsersWhoLike.add(new BasicUserDto(user.getId(), user.getUsername()));
            }
        }

        List<BasicUserDto> basicUsersWhoDislike = new ArrayList<>();
        if (usersWhoDislike != null){
            for (User user : usersWhoDislike){
                basicUsersWhoDislike.add(new BasicUserDto(user.getId(), user.getUsername()));
            }
        }

        return new PostDtoResponse(id, title, content, author.getUsername(), totalLikes,
                totalDislikes, date, fixed, answers, totalAnswers, filesUrl, basicUsersWhoLike, basicUsersWhoDislike);
    }

    public void refreshLikes(){
        totalLikes = usersWhoLike.size();
        totalDislikes = usersWhoDislike.size();
    }

    public void refreshTotalAnswers(){
        totalAnswers = answers.size();
    }
}
