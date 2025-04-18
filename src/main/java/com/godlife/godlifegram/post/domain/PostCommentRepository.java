package com.godlife.godlifegram.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    void deleteByPost(Post post);
}
