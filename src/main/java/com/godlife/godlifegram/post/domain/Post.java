package com.godlife.godlifegram.post.domain;

import com.godlife.godlifegram.common.domain.BaseEntity;
import com.godlife.godlifegram.user.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static Post upload(String content, User user) {
        Post post = new Post();
        post.content = content;
        post.user = user;
        return post;
    }

}
