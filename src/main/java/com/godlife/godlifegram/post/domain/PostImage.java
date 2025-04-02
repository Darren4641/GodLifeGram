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
@Table(name = "post_images")
public class PostImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // S3 URL

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

}
