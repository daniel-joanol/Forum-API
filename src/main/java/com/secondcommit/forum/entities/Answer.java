package com.secondcommit.forum.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User author;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "POST_FILES",
            joinColumns = {
                    @JoinColumn(name = "POST_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private Set<File> files = new HashSet<>();

    private Date date = new Date();

    private boolean fixed = false;

    @Column(name = "total_likes")
    private int totalLikes = 0;

    @Column(name = "total_dislikes")
    private int totalDislikes = 0;

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

    //Constructors
    public Answer() {
    }

    public Answer(String content, User author) {
        this.content = content;
        this.author = author;
        date = new Date();
    }

    public Answer(String content, User author, Set<File> files) {
        this.content = content;
        this.author = author;
        this.files = files;
        date = new Date();
    }

    public void addFile(File file){
        files.add(file);
    }

    public void removeFile(File file){
        files.remove(file);
    }

    public void addUsersWhoLike(User user){
        usersWhoLike.add(user);
        setTotalLikes(usersWhoLike.size());
    }

    public void removeUsersWhoLike(User user){
        usersWhoLike.remove(user);
        setTotalLikes(usersWhoLike.size());
    }

    public void addUsersWhoDislike(User user){
        usersWhoDislike.add(user);
        setTotalDislikes(usersWhoDislike.size());
    }

    public void removeUsersWhoDislike(User user){
        usersWhoDislike.remove(user);
        setTotalDislikes(usersWhoDislike.size());
    }
}
