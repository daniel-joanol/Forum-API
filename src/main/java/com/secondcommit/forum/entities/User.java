package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.BasicSubjectDtoResponse;
import com.secondcommit.forum.dto.UserResponseDto;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FOLLOWS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private List<Subject> followsSubject = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_HASACCESS_SUBJECTS",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "SUBJECT_ID") })
    private List<Subject> hasAccess = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FOLLOWS_POST",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "POST_ID") })
    private List<Post> followsPost = new ArrayList<>();

    //Constructors
    public User() {
    }

    public User(String email, String username, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, List<Role> roles, File avatar) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, List<Role> roles, List<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.hasAccess = hasAccess;
        this.roles = roles;
        this.password = password;
    }

    public User(String email, String username, String password, List<Role> roles, File avatar, List<Subject> hasAccess) {
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        this.roles = roles;
        this.password = password;
        this.hasAccess = hasAccess;
    }

    public UserResponseDto getDtoFromUser(String message){

        String urlAvatar = "";
        if (avatar != null) urlAvatar = avatar.getUrl();

        List<BasicSubjectDtoResponse> hasAccessDto = new ArrayList<>();
        if (hasAccess != null)
            for (Subject subject : hasAccess){
                hasAccessDto.add(new BasicSubjectDtoResponse(subject.getId(), subject.getName()));
            }

        List<BasicSubjectDtoResponse> followSubjectDto = new ArrayList<>();
        if (followsSubject != null)
            for (Subject subject : followsSubject){
                followSubjectDto.add(new BasicSubjectDtoResponse(subject.getId(), subject.getName()));
            }

        return new UserResponseDto(
                id, email, username, urlAvatar, hasAccessDto, isActivated, followSubjectDto, followsPost, message
        );
    }
}
