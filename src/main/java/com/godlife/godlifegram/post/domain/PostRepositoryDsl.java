package com.godlife.godlifegram.post.domain;

import com.godlife.godlifegram.post.ui.dto.response.ViewResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hibernate.query.SortDirection;
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
                        postLike.id.countDistinct(),
                        user.nickname,
                        imageUrls,
                        isLiked,
                        post.createdDate
                )
                .from(post)
                .join(post.user, user)
                .leftJoin(postLike).on(postLike.post.id.eq(post.id))
                .leftJoin(postImage).on(postImage.post.id.eq(post.id))
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
                    it.get(postLike.id.countDistinct()),
                    it.get(user.nickname),
                    images,
                    it.get(isLiked) != null && it.get(isLiked) == 1,
                    it.get(post.createdDate)
            );
        })
        .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, count);
    }

    private OrderSpecifier<?> getOrderSpecifier(String keyword, Order direction) {

        return switch (keyword) {
            case "like" -> new OrderSpecifier<>(direction, post.likeCount);
            case "view" -> new OrderSpecifier<>(direction, post.viewCount);
            case "created" -> new OrderSpecifier<>(direction, post.createdDate);
            default -> new OrderSpecifier<>(direction, post.createdDate);
        };
    }
}
