package com.godlife.godlifegram.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findByPostId(Long id);

    void deleteByPost(Post post);
}
