package com.godlife.godlifegram.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUuid(String uuid);

    Optional<PostLike> findByUuidAndPost(String uuid, Post post);
}
