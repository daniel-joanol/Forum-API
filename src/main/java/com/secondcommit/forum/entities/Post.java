package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.PostDto;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity that manages the posts in the database
 */
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User author;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int totalLikes = 0;

    @Column
    private int totalDislikes = 0;

    @Column
    private Date date = new Date();

    @Column
    private boolean fixed = false;

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

    //Constructors

    public Post() {
    }

    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        date = new Date();
    }

    public Post(User author, String title, String content, Set<File> files) {
        this.author = author;
        this.title = title;
        this.content = content;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return totalLikes;
    }

    public void setLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public int getDislikes() {
        return totalDislikes;
    }

    public void setDislikes(int totalDislikes) {
        this.totalDislikes = totalDislikes;
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

    public Set<User> getUsersWhoLike() {
        return usersWhoLike;
    }

    public void setUsersWhoLike(Set<User> usersWhoLike) {
        this.usersWhoLike = usersWhoLike;
    }

    public void addUsersWhoLike(User user) {
        usersWhoLike.add(user);
        setLikes(usersWhoLike.size());
    }

    public void removeUsersWhoLike(User user) {
        usersWhoLike.remove(user);
        setLikes(usersWhoLike.size());
    }

    public Set<User> getUsersWhoDislike() {
        return usersWhoDislike;
    }

    public void setUsersWhoDislike(Set<User> usersWhoDislike) {
        this.usersWhoDislike = usersWhoDislike;
    }

    public void addUsersWhoDislike(User user) {
        usersWhoDislike.add(user);
        setDislikes(usersWhoDislike.size());
    }

    public void removeUsersWhoDislike(User user){
        usersWhoDislike.remove(user);
        setDislikes(usersWhoDislike.size());
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

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer){
        answers.add(answer);
        setTotalAnswers(answers.size());
    }

    public void removeAnswer(Answer answer){
        answers.remove(answer);
        setTotalAnswers(answers.size());
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }
}
