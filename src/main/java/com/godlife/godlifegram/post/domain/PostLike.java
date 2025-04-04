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
@Table(name = "post_likes")
public class PostLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", length = 100)
    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    public static PostLike like(String uuid, Post post) {
        PostLike postLike = new PostLike();
        postLike.uuid = uuid;
        postLike.post = post;
        return postLike;
    }

}
