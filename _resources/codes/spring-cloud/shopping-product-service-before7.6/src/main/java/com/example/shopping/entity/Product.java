package com.example.shopping.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 商品
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "tbProduct")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // 商品 id
    @Id
    @GeneratedValue
    private Long id;
    // 商品名
    private String name;
    // 封面图片
    private String coverImage;
    // 商品价格（分）
    private int price;
}
