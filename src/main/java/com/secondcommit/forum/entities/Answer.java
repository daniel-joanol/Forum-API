package com.secondcommit.forum.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity that manages the answer in the database
 */
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

    //Getters and Setters (also remove and add for the Sets)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public void addFile(File file){
        files.add(file);
    }

    public void removeFile(File file){
        files.remove(file);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(int totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public Set<User> getUsersWhoLike() {
        return usersWhoLike;
    }

    public void setUsersWhoLike(Set<User> usersWhoLike) {
        this.usersWhoLike = usersWhoLike;
    }

    public void addUsersWhoLike(User user){
        usersWhoLike.add(user);
        setTotalLikes(usersWhoLike.size());
    }

    public void removeUsersWhoLike(User user){
        usersWhoLike.remove(user);
        setTotalLikes(usersWhoLike.size());
    }

    public Set<User> getUsersWhoDislike() {
        return usersWhoDislike;
    }

    public void setUsersWhoDislike(Set<User> usersWhoDislike) {
        this.usersWhoDislike = usersWhoDislike;
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
