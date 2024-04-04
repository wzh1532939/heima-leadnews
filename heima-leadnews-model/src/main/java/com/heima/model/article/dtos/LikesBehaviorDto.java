package com.heima.model.article.dtos;

import lombok.Data;

@Data
public class LikesBehaviorDto {
    private Long articleId;
    private Short type;
    private Short operation;
}
