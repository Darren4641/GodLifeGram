package com.godlife.godlifegram.post.ui.dto.request;

import com.querydsl.core.types.Order;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewPostRequestDto {
    @Pattern(
        regexp = "^$|like|view|created",
        message = "sortKeyword은 빈 값이거나 'like', 'view', 'created' 만 가능합니다."
    )
    private String sortKeyword = "";
    private Order sortDirection = Order.ASC;
    private int page = 0;
    private int size = 10;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }
}


