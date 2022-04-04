package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.BasicSubjectDtoResponse;
import com.secondcommit.forum.dto.UserResponseDto;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity that manages the users in the database
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_activated")
    private Boolean isActivated = false;

    @Column(name = "time_stamp")
    private Timestamp timeStamp;

    @Column(name = "validation_code")
    private Integer validationCode;

    @Column(name = "activation_code")
    private Integer activationCode;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_AVATAR",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "FILE_ID") })
    private File avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FOLLOWS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private Set<Subject> followsSubject = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_HASACCESS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private Set<Subject> hasAccess = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
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

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public void removeFollowsSubject(Subject subject){
        followsSubject.remove(subject);
    }

    public void addFollowsSubject(Subject subject) {
        followsSubject.add(subject);
    }

    public void removeAccess(Subject subject){
        hasAccess.remove(subject);
    }

    public void addAccess(Subject subject) {
        hasAccess.add(subject);
    }

    public void removeFollowsPost(Post post) {
        followsPost.remove(post);
    }

    public void addFollowsPost(Post post){
        followsPost.add(post);
    }

    public UserResponseDto getDtoFromUser(String message){

        String urlAvatar = "";
        if (avatar != null) urlAvatar = avatar.getUrl();

        Set<BasicSubjectDtoResponse> hasAccessDto = new HashSet<>();
        if (hasAccess != null)
            for (Subject subject : hasAccess){
                hasAccessDto.add(new BasicSubjectDtoResponse(subject.getId(), subject.getName()));
            }

        Set<BasicSubjectDtoResponse> followSubjectDto = new HashSet<>();
        if (followsSubject != null)
            for (Subject subject : followsSubject){
                followSubjectDto.add(new BasicSubjectDtoResponse(subject.getId(), subject.getName()));
            }

        return new UserResponseDto(
                id, email, username, urlAvatar, hasAccessDto, isActivated, followSubjectDto, followsPost, message
        );
    }
}
