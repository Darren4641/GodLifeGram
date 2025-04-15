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

    @Column(name = "like_goal")
    private Long likeGoal = 3L;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public static Post upload(String content, Long likeGoal, User user) {
        Post post = new Post();
        post.content = content;
        post.likeGoal = likeGoal;
        post.user = user;
        return post;
    }

    public void reUpload(String content, Long likeGoal, User user) {
        this.content = content;
        this.likeGoal = likeGoal;
        this.user = user;
    }

}
