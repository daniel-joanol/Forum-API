package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.BasicUserDto;
import com.secondcommit.forum.dto.PostDtoResponse;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

/**
 * Entity that manages the posts in the database
 */
@Data
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
    private int totalLikes = 0;

    @Column(name = "total_dislikes")
    private int totalDislikes = 0;

    private Date date = new Date();

    private boolean fixed = false;

    @ManyToMany(mappedBy ="followsPost")
    private List<User> usersFollowing = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_WHOLIKE_POST",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID") })
    private Set<User> usersWhoLike = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USERS_WHODISLIKE_POST",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "USER_ID") })
    private Set<User> usersWhoDislike = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "POST_FILES",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private Set<File> files = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "POST_ANSWERS",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ANSWERS_ID") })
    private Set<Answer> answers = new HashSet<>();

    @Column
    private int totalAnswers = 0;

    @ManyToOne
    private Module module;

    //Constructors

    public Post() {
    }

    public Post(User author, String title, String content, Module module) {
        this.author = author;
        this.title = title;
        this.content = content;
        date = new Date();
        this.module = module;
    }

    public Post(User author, String title, String content, Set<File> files, Module module) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.files = files;
        date = new Date();
        this.module = module;
    }

    public void addUsersWhoLike(User user) {
        usersWhoLike.add(user);
        setTotalLikes(usersWhoLike.size());
    }

    public void removeUsersWhoLike(User user) {
        usersWhoLike.remove(user);
        setTotalLikes(usersWhoLike.size());
    }

    public void addUsersWhoDislike(User user) {
        usersWhoDislike.add(user);
        setTotalDislikes(usersWhoDislike.size());
    }

    public void removeUsersWhoDislike(User user){
        usersWhoDislike.remove(user);
        setTotalDislikes(usersWhoDislike.size());
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
        setTotalAnswers(answers.size());
    }

    public void removeAnswer(Answer answer){
        answers.remove(answer);
        setTotalAnswers(answers.size());
    }

    public PostDtoResponse getDtoFromPost(){

        Set<String> filesUrl = new HashSet<>();
        if (files != null){
            for (File file : files){
                filesUrl.add(file.getUrl());
            }
        }

        Set<BasicUserDto> basicUsersWhoLike = new HashSet<>();
        if (usersWhoLike != null){
            for (User user : usersWhoLike){
                basicUsersWhoLike.add(new BasicUserDto(user.getId(), user.getUsername()));
            }
        }

        Set<BasicUserDto> basicUsersWhoDislike = new HashSet<>();
        if (usersWhoDislike != null){
            for (User user : usersWhoDislike){
                basicUsersWhoDislike.add(new BasicUserDto(user.getId(), user.getUsername()));
            }
        }

        return new PostDtoResponse(id, title, content, author.getUsername(), totalLikes,
                totalDislikes, date, fixed, answers, totalAnswers, filesUrl, basicUsersWhoLike, basicUsersWhoDislike);
    }
}
