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
@Table(name = "post_comments")
public class PostComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static PostComment writeComment(String content, User user, Post post) {
        PostComment postComment = new PostComment();
        postComment.content = content;
        postComment.user = user;
        postComment.post = post;
        return postComment;
    }

}
