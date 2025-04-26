package com.example.shopping.api;

import com.example.shopping.dto.ProductCommentDto;
import com.example.shopping.dto.ProductDto;
import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.Product;
import com.example.shopping.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ProductEndpointImpl implements ProductEndpoint {

    @Autowired
    private ProductService productService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

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
        return circuitBreakerFactory.create("user-service").run(
                // 微服务请求
                () -> productService.findAllByProduct(id).stream()
                        .map(comment -> {
                            UserDto user = restTemplate.getForObject("http://SHOPPING-USER-SERVICE/users/{id}",
                                    UserDto.class,
                                    comment.getAuthorId());
                            return ProductCommentDto.build(comment, product, user);
                        })
                        .toList(),
                // 服务降级 fallback
                e -> List.of());
    }
}
