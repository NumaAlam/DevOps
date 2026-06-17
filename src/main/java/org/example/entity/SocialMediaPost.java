package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "SocialMediaPosts")
@Getter
@Setter
@NoArgsConstructor
public class SocialMediaPost {

    @Id
    @Column(name = "PostID")
    private Integer postId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Platform")
    private String platform;

    @Column(name = "PostDate")
    private LocalDate postDate;

    @Column(name = "ContentType")
    private String contentType;

    @Column(name = "Likes")
    private Integer likes;

    @Column(name = "Comments")
    private Integer comments;

    @Column(name = "Shares")
    private Integer shares;

    @Column(name = "Followers")
    private Integer followers;

    @Column(name = "Hashtag")
    private String hashtag;
}
