package com.example.shopping.service;

import com.example.shopping.entity.Product;
import com.example.shopping.entity.ProductComment;
import com.example.shopping.repository.ProductCommentRepository;
import com.example.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductCommentRepository productCommentRepository;

    @Override
    public Page<Product> getPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product load(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public List<ProductComment> findAllByProduct(Long productId) {
        return productCommentRepository.findByProductIdOrderByCreated(productId);
    }
}
