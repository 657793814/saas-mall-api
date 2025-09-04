package com.liuzd.soft.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.liuzd.soft.dto.PChinaRegionDto;
import com.liuzd.soft.dto.product.PProductCategoryDto;
import com.liuzd.soft.vo.page.PageResult;
import com.liuzd.soft.vo.product.ProductDetailResp;
import com.liuzd.soft.vo.product.ProductPageReq;
import com.liuzd.soft.vo.product.ProductPageResp;

import java.util.List;

/**
 * @author: liuzd
 * @date: 2025/8/24
 * @email: liuzd2025@qq.com
 * @desc
 */
public interface ProductService {

    PageResult<ProductPageResp> productList(ProductPageReq productPageReq);

    List<PProductCategoryDto> categoryList();

    ProductDetailResp productDetail(Integer productId) throws JsonProcessingException;

    List<PChinaRegionDto> areaData();

}