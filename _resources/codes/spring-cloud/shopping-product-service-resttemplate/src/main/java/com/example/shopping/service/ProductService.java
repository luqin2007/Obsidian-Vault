package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.entity.ProductComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    /**
     * 获取商品的分页数据
     *
     * @param pageable 分页参数
     * @return 分页数据
     */
    Page<Product> getPage(Pageable pageable);

    /**
     * 获取指定的商品配置
     *
     * @param id 商品 id
     */
    Product load(Long id);

    /**
     * 获取指定商品的评论列表
     *
     * @param productId 商品 id
     */
    List<ProductComment> findAllByProduct(Long productId);
}
