package com.godlife.godlifegram.post.domain;

import com.godlife.godlifegram.post.ui.dto.response.ViewCommentResponseDto;
import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.godlife.godlifegram.post.domain.QPost.post;
import static com.godlife.godlifegram.post.domain.QPostComment.postComment;
import static com.godlife.godlifegram.post.domain.QPostImage.postImage;
import static com.godlife.godlifegram.post.domain.QPostLike.postLike;
import static com.godlife.godlifegram.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostRepositoryDsl {
    private final JPAQueryFactory queryFactory;

    public Page<ViewResponseDto> getPostsOfPage(Pageable pageable, String sortKeyword, String uuid, Order sortDirection) {
        // 정렬 조건 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortKeyword, sortDirection);

        StringTemplate imageUrls = Expressions.stringTemplate("group_concat(distinct {0})", postImage.imageUrl);

        Expression<Integer> isLiked = ExpressionUtils.as(
                new CaseBuilder()
                        .when(postLike.uuid.eq(uuid)).then(1)
                        .otherwise(0)
                        .max(),
                "isLiked"
        );

        List<Tuple> result = queryFactory
                .select(
                        post.id,
                        post.content,
                        post.likeGoal,
                        postLike.id.countDistinct(),
                        postComment.id.countDistinct(),
                        user.nickname,
                        imageUrls,
                        isLiked,
                        post.createdDate
                )
                .from(post)
                .join(post.user, user)
                .leftJoin(postLike).on(postLike.post.id.eq(post.id))
                .leftJoin(postImage).on(postImage.post.id.eq(post.id))
                .leftJoin(postComment).on(postComment.post.id.eq(post.id))
                .groupBy(post.id)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(post.id.count())
                .from(post)
                .fetchOne()).orElse(0L);

        List<ViewResponseDto> content = result.stream().map(it -> {
            String imageUrlConcat = it.get(imageUrls);

            List<String> images = (imageUrlConcat != null)
                    ? Arrays.stream(imageUrlConcat.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toList()
                    : Collections.emptyList();

            return new ViewResponseDto(
                    it.get(post.id),
                    it.get(post.content),
                    it.get(postComment.id.countDistinct()),
                    it.get(postLike.id.countDistinct()),
                    it.get(post.likeGoal),
                    it.get(user.nickname),
                    images,
                    it.get(isLiked) != null && it.get(isLiked) == 1,
                    it.get(post.createdDate)
            );
        })
        .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, count);
    }

    public Optional<ViewResponseDto.ViewDetailResponseDto> getPost(Long id, String uuid) {
        StringTemplate imageUrls = Expressions.stringTemplate("group_concat(distinct {0})", postImage.imageUrl);

        Expression<Boolean> isLiked = ExpressionUtils.as(
                new CaseBuilder()
                        .when(postLike.uuid.eq(uuid)).then(true)
                        .otherwise(false)
                        .max(),
                "isLiked"
        );

        ViewResponseDto.ViewDetailResponseDto dto = queryFactory
                .select(
                        Projections.constructor(
                                ViewResponseDto.ViewDetailResponseDto.class,
                                post.id,
                                post.content,
                                postComment.id.countDistinct(),
                                postLike.id.countDistinct(),
                                post.likeGoal,
                                user.nickname,
                                imageUrls,
                                isLiked,
                                post.createdDate
                        )
                )
                .from(post)
                .join(post.user, user)
                .leftJoin(postLike).on(postLike.post.id.eq(post.id))
                .leftJoin(postImage).on(postImage.post.id.eq(post.id))
                .leftJoin(postComment).on(postComment.post.id.eq(post.id))
                .where(post.id.eq(id))
                .limit(1)
                .fetchFirst();

        return (dto == null || dto.getId() == null) ? Optional.empty() : Optional.of(dto);
    }


    public Page<ViewCommentResponseDto> getCommentsOfPage(Pageable pageable, Long postId) {
        List<Tuple> result = queryFactory
                .select(
                        postComment.id,
                        postComment.content,
                        user.id,
                        user.nickname,
                        postComment.createdDate
                )
                .from(postComment)
                .join(postComment.user, user)
                .where(postComment.post.id.eq(postId))
                .orderBy(postComment.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(queryFactory
                .select(postComment.id.count())
                .from(postComment)
                .where(postComment.post.id.eq(postId))
                .fetchOne()).orElse(0L);

        List<ViewCommentResponseDto> content = result.stream().map(it -> {

                    return new ViewCommentResponseDto(
                            it.get(postComment.id),
                            it.get(postComment.content),
                            it.get(user.id),
                            it.get(user.nickname),
                            it.get(postComment.createdDate)
                    );
                })
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, count);
    }

    private OrderSpecifier<?> getOrderSpecifier(String keyword, Order direction) {

        return switch (keyword) {
            case "like" -> new OrderSpecifier<>(direction, postLike.id.countDistinct());
            case "created" -> new OrderSpecifier<>(direction, post.createdDate);
            default -> new OrderSpecifier<>(direction, post.createdDate);
        };
    }
}
