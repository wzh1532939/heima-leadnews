package com.heima.model.article.dtos;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ArticleHomeDto {
    Date maxBehotTime;
    Date minBehotTime;
    Integer size;
    String tag;
}
