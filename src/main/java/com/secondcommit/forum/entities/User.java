package com.secondcommit.forum.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity that manages the users in the database
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String email;

    @Column
    private String username;

    @Column
    private int hashedPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_FOLLOWS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private Set<Subject> followsSubject = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_HASACCESS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private Set<Subject> hasAccess = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_FOLLOWS_POST",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "POST_ID") })
    private Set<Post> followsPost = new HashSet<>();

    //Constructors
    public User() {
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        hashedPassword = generateHashCode(password, email);
    }

    //Getters and Setters (also remove and add for the Sets)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(int hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setHashedPassword(String password) {
        hashedPassword = generateHashCode(password, email);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public Set<Subject> getFollowsSubject() {
        return followsSubject;
    }

    public void setFollowsSubject(Set<Subject> followsSubject) {
        this.followsSubject = followsSubject;
    }

    public void removeFollowsSubject(Subject subject){
        followsSubject.remove(subject);
    }

    public void addFollowsSubject(Subject subject) {
        followsSubject.add(subject);
    }

    public Set<Subject> getHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(Set<Subject> hasAccess) {
        this.hasAccess = hasAccess;
    }

    public void removeAccess(Subject subject){
        hasAccess.remove(subject);
    }

    public void addAccess(Subject subject) {
        hasAccess.add(subject);
    }

    public Set<Post> getFollowsPost() {
        return followsPost;
    }

    public void setFollowsPost(Set<Post> followsPost) {
        this.followsPost = followsPost;
    }

    public void removeFollowsPost(Post post) {
        followsPost.remove(post);
    }

    public void addFollowsPost(Post post){
        followsPost.add(post);
    }

    //Other methods

    /**
     * Generates a new hashCode from the password and email
     * @param password String
     * @param email String
     * @return int hashcode
     */
    public int generateHashCode(String password, String email){
        return password.hashCode()*email.hashCode();
    }
}
