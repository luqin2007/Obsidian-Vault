package com.example.shopping.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品评论
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tbProduct_Comment")
public class ProductComment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 评论 id
    @Id
    @GeneratedValue
    private Long id;
    // 商品 id
    private Long productId;
    // 评论作者 id
    private Long authorId;
    // 评论内容
    private String content;
    // 评论时间
    private Date created;
}
