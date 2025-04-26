package com.example.shopping.api;

import com.example.shopping.dto.ProductCommentDto;
import com.example.shopping.dto.ProductDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Api("商品管理相关Api")
@RequestMapping("/products")
public interface ProductEndpoint {

    @ApiOperation(value = "获取商品分页数据", notes = "获取商品分页数据", httpMethod = "GET", tags = "商品管理相关Api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页，从0开始，默认为第0页", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每一页记录数的大小，默认为20", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sort",
                    value = "排序，格式:property,property(,ASC|DESC)，如sort=firstname&sort=lastname,desc",
                    dataType = "String",
                    paramType = "query")
    })
    @GetMapping
    List<ProductDto> list(Pageable pageable);

    @ApiOperation(value = "获取商品详情数据", notes = "获取商品详情数据", httpMethod = "GET", tags = "商品管理相关Api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品的主键", dataType = "int", paramType = "path")
    })
    @GetMapping("/{id}")
    ProductDto detail(@PathVariable Long id);

    @ApiOperation(value = "获取商品的评论列表", notes = "获取商品的评论列表", httpMethod = "GET", tags = "商品管理相关Api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品的主键", dataType = "int", paramType = "path")
    })
    @GetMapping("/{id}/comments")
    List<ProductCommentDto> comments(@PathVariable Long id);
}
