package com.secondcommit.forum.entities;

import com.secondcommit.forum.dto.ModuleDtoResponse;
import com.secondcommit.forum.dto.SubjectDtoResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity that manages the subjects in the database
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "total_modules")
    private Integer totalModules = 0;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "SUBJECT_AVATAR",
            joinColumns = {
                    @JoinColumn(name = "SUBJECT_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "AVATAR_ID") })
    private File avatar;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "SUBJECT_MODULES",
            joinColumns = {
                    @JoinColumn(name = "SUBJECT_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "MODULE_ID") })
    private List<Module> modules = new ArrayList<>();

    @ManyToMany(mappedBy = "hasAccess")
    private List<User> usersWithAccess = new ArrayList<>();

    @ManyToMany(mappedBy = "followsSubject")
    private List<User> usersFollowing = new ArrayList<>();

    //Constructors
    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, List<Module> modules) {
        this.name = name;
        this.modules = modules;
    }

    public SubjectDtoResponse getDtoFromSubject(){
        Set<ModuleDtoResponse> modulesDto = new HashSet<>();
        String backupAvatar = "";

        if (modules != null)
            for (Module module : modules){
                modulesDto.add(new ModuleDtoResponse(module.getId(), module.getName(), module.getDescription(), module.getTotalQuestions()));
            }

        if (avatar != null) backupAvatar = avatar.getUrl();

        return new SubjectDtoResponse(id, name, backupAvatar , modulesDto);
    }

    public void refreshTotalModules(){
        totalModules = modules.size();
    }
}
