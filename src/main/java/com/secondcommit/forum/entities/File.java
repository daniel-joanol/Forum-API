package com.secondcommit.forum.entities;

import lombok.Data;

import javax.persistence.*;

/**
 * Entity that manages the files in the database
 */
@Data
@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "cloudinary_id", nullable = false)
    private String cloudinaryId;

    public File() {
    }

    public File(String url, String cloudinaryId) {
        this.url = url;
        this.cloudinaryId = cloudinaryId;
    }
}
