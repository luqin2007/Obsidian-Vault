package com.example.shopping.dto;

import com.example.shopping.entity.ProductComment;

import java.util.Date;

/**
 * 商品评论
 *
 * @param id      评论 id
 * @param product 评论商品
 * @param author  评论作者
 * @param content 评论内容
 * @param created 评论时间
 */
public record ProductCommentDto(Long id, ProductDto product, UserDto author, String content, Date created) {

    public static ProductCommentDto build(ProductComment comment, ProductDto product, UserDto author) {
        return new ProductCommentDto(comment.getId(), product, author, comment.getContent(), comment.getCreated());
    }
}
