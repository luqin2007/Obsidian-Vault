package com.example.shopping.api;

import com.example.shopping.model.ProductCommentDto;
import com.example.shopping.model.ProductDto;
import com.example.shopping.model.UserDto;
import com.example.shopping.entity.Product;
import com.example.shopping.service.ProductService;
import com.example.shopping.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductEndpointImpl implements ProductEndpoint {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @Override
    public List<ProductDto> list(Pageable pageable) {
        return productService.getPage(pageable)
                .getContent().stream()
                .map(ProductDto::fromProduct)
                .toList();
    }

    @Override
    public ProductDto detail(@PathVariable Long id) {
        Product product = productService.load(id);
        return ProductDto.fromProduct(product);
    }

    @Override
    public List<ProductCommentDto> comments(@PathVariable Long id) {
        ProductDto product = ProductDto.fromProduct(productService.load(id));
        return productService.findAllByProduct(id).stream()
                .map(comment -> {
                    UserDto user = userService.detail(comment.getAuthorId());
                    return ProductCommentDto.build(comment, product, user);
                })
                .toList();
    }
}
