package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.NewUserResponse;

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
    private String password;

    @Column
    private Boolean isActivated = false;

    @Column
    private Integer validationCode;

    @Column
    private Integer activationCode;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_AVATAR",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private File avatar;

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

    public User(String email, String username, String password, Set<Role> roles) {
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, Set<Role> roles, File avatar) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, Set<Role> roles, Set<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.hasAccess = hasAccess;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, Set<Role> roles, File avatar, Set<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.roles = roles;
        this.password = password;
        this.hasAccess = hasAccess;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Boolean isActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public Integer getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(Integer validationCode) {
        this.validationCode = validationCode;
    }

    public Integer getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(Integer activationCode) {
        this.activationCode = activationCode;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    //Other methods

    public NewUserResponse getDtoFromUser(){
        return new NewUserResponse(
                id, email, username, avatar, hasAccess, isActivated, followsSubject, followsPost
        );
    }
}
