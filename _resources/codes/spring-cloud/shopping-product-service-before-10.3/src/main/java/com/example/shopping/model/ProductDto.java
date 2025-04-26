package com.example.shopping.model;

import com.example.shopping.entity.Product;

/**
 * 商品
 *
 * @param id         商品 id
 * @param name       商品名
 * @param coverImage 封面图片
 * @param price      商品价格（分）
 */
public record ProductDto(Long id, String name, String coverImage, int price) {

    public static ProductDto fromProduct(Product product) {
        if (product == null) return null;
        return new ProductDto(product.getId(), product.getName(), product.getCoverImage(), product.getPrice());
    }
}
