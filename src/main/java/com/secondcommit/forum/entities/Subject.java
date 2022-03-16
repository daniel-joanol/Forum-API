package com.secondcommit.forum.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Entity that manages the subjects in the database
 */
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer totalModules = 0;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "SUBJECT_AVATAR",
            joinColumns = {
                    @JoinColumn(name = "SUBJECT_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "AVATAR_ID") })
    private File avatar;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "SUBJECT_POSTS",
            joinColumns = {
                    @JoinColumn(name = "SUBJECT_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "MODULE_ID") })
    private Set<Module> modules;

    public Subject() {
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject(String name, Set<Module> modules) {
        this.name = name;
        this.modules = modules;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalModules() {
        return totalModules;
    }

    public void setTotalModules(Integer totalModules) {
        this.totalModules = totalModules;
    }

    public File getAvatar() {
        return avatar;
    }

    public void setAvatar(File avatar) {
        this.avatar = avatar;
    }

    public Set<Module> getModules() {
        return modules;
    }

    public void setModules(Set<Module> modules) {
        this.modules = modules;
    }

    public void addModule(Module module){
        modules.add(module);
        totalModules = modules.size();
    }

    public void removeModule(Module module){
        modules.remove(module);
        totalModules = modules.size();
    }
}
