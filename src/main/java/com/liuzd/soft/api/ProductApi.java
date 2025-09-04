package com.liuzd.soft.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuzd.soft.annotation.NoLogin;
import com.liuzd.soft.dto.PChinaRegionDto;
import com.liuzd.soft.dto.product.PProductCategoryDto;
import com.liuzd.soft.service.ProductService;
import com.liuzd.soft.vo.ResultMessage;
import com.liuzd.soft.vo.page.PageResult;
import com.liuzd.soft.vo.product.ProductDetailResp;
import com.liuzd.soft.vo.product.ProductPageReq;
import com.liuzd.soft.vo.product.ProductPageResp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/8/12
 * @email: liuzd2025@qq.com
 * @desc
 */
@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
@NoLogin
public class ProductApi {

    final ProductService productService;

    @PostMapping(path = "/product_list")
    public ResultMessage<PageResult<ProductPageResp>> productList(@Valid @RequestBody ProductPageReq productPageReq) {
        return ResultMessage.success(productService.productList(productPageReq));
    }

    @RequestMapping(path = "/product_detail")
    public ResultMessage<ProductDetailResp> productDetail(@RequestParam("id") Integer productId) throws JsonProcessingException {
        return ResultMessage.success(productService.productDetail(productId));
    }

    @RequestMapping(path = "/category_list")
    public ResultMessage<List<PProductCategoryDto>> categoryList() {
        return ResultMessage.success(productService.categoryList());
    }

    @RequestMapping(path = "/area_data")
    public ResultMessage<List<PChinaRegionDto>> areaData() {
        return ResultMessage.success(productService.areaData());
    }

}
