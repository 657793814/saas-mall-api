package com.liuzd.soft.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzd.soft.config.ProjectProperties;
import com.liuzd.soft.dao.PChinaRegionDao;
import com.liuzd.soft.dao.PItemsDao;
import com.liuzd.soft.dao.PProductCategoryDao;
import com.liuzd.soft.dao.PProductsDao;
import com.liuzd.soft.dto.PChinaRegionDto;
import com.liuzd.soft.dto.product.PProductCategoryDto;
import com.liuzd.soft.entity.PChinaRegionEntity;
import com.liuzd.soft.entity.PItemsEntity;
import com.liuzd.soft.entity.PProductCategoryEntity;
import com.liuzd.soft.entity.PProductsEntity;
import com.liuzd.soft.enums.RetEnums;
import com.liuzd.soft.exception.MyException;
import com.liuzd.soft.service.ProductService;
import com.liuzd.soft.service.es.ProductSearchService;
import com.liuzd.soft.vo.page.PageResult;
import com.liuzd.soft.vo.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * saas 商家后台产品逻辑
 *
 * @author: liuzd
 * @date: 2025/8/24
 * @email: liuzd2025@qq.com
 * @desc
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    final ObjectMapper objectMapper;
    final MinioService minioService;
    final PProductsDao pProductsDao;
    final PItemsDao pItemsDao;
    final PProductCategoryDao pProductCategoryDao;
    final Optional<ProductSearchService> productSearchService;
    final ProjectProperties projectProperties;
    final PChinaRegionDao pChinaRegionDao;

    @Override
    public PageResult<ProductPageResp> productList(ProductPageReq req) {
        PageResult<ProductPageResp> pageResult = new PageResult<>();
        Page<PProductsEntity> resultPage = new Page<>(req.getCurrent(), req.getSize());
        if (projectProperties.isEsEnabled()) {
            resultPage = productSearchService.get().searchProductsPage(req);
        } else {
            QueryWrapper<PProductsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.like(StringUtils.isNotBlank(req.getName()), "name", req.getName());
            queryWrapper.like(StringUtils.isNotBlank(req.getCode()), "code", req.getCode());
            queryWrapper.eq(req.getEnable() != null, "enable", req.getEnable());
            queryWrapper.orderByDesc("id");
            resultPage = pProductsDao.selectPage(resultPage, queryWrapper);
        }

        //查询sku
        List<Integer> productIds = resultPage.getRecords().stream().map(PProductsEntity::getId).collect(Collectors.toList());
        QueryWrapper<PItemsEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.in("product_id", productIds);
        List<PItemsEntity> itemsList = pItemsDao.selectList(queryWrapper1);
        Map<Integer, List<PItemsEntity>> itemsMap = itemsList.stream().collect(Collectors.groupingBy(PItemsEntity::getProductId));


        List<PProductCategoryEntity> categoryList = pProductCategoryDao.selectList(new QueryWrapper<PProductCategoryEntity>().eq("level", 1));
        Map<Integer, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(PProductCategoryEntity::getId, PProductCategoryEntity::getName));

        //处理返回
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setCurrent((int) resultPage.getCurrent());
        pageResult.setSize((int) resultPage.getSize());
        pageResult.setRecords(resultPage.getRecords().stream().map(product -> {
            ProductPageResp productPageResp = new ProductPageResp();

            productPageResp.setProductId(product.getId());
            productPageResp.setCode(product.getCode());
            productPageResp.setName(product.getName());
            productPageResp.setEnable(product.getEnable());
            productPageResp.setSaleNum(product.getSaleNum());
            productPageResp.setCategory(categoryMap.get(product.getOneCategory()));
            String productImgUrl = "";
            if (StringUtils.isNotBlank(product.getImgUrls())) {
                try {
                    List<String> imgUrlList = objectMapper.readValue(product.getImgUrls(), new TypeReference<List<String>>() {
                    });
                    productImgUrl = imgUrlList.get(0);
                    productImgUrl = StringUtils.isNotBlank(productImgUrl) ? minioService.getFileUrl(productImgUrl) : "";
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            productPageResp.setImgUrl(productImgUrl);

            if (itemsMap.containsKey(product.getId())) {
                List<PItemsEntity> skus = itemsMap.get(product.getId());
                productPageResp.setSalePrice(skus.get(0).getSalePrice());
                productPageResp.setCostPrice(skus.get(0).getCostPrice());
            }

            return productPageResp;
        }).collect(Collectors.toList()));

        return pageResult;
    }

    @Override
    public ProductDetailResp productDetail(Integer productId) throws JsonProcessingException {
        QueryWrapper<PProductsEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", productId);
        PProductsEntity product = pProductsDao.selectOne(queryWrapper);
        Assert.notNull(product, () -> MyException.exception(RetEnums.PRODUCT_NOT_EXIST));

        ProductDetailResp productDetailResp = new ProductDetailResp();
        productDetailResp.setId(product.getId());
        productDetailResp.setName(product.getName());
        productDetailResp.setDesc(product.getDesc());
        productDetailResp.setDetail(product.getDetail());
        productDetailResp.setShippingTemplateId(product.getShippingTemplateId());
        productDetailResp.setCategoryIds(new ArrayList<Integer>() {
            {
                add(product.getOneCategory());
                add(product.getTwoCategory());
                add(product.getThreeCategory());
            }
        });

        //img 处理
        List<String> imageUrls = new ArrayList<>();
        List<Map<String, String>> imgs = new ArrayList<>();
        if (StringUtils.isNotBlank(product.getImgUrls())) {
            imageUrls = objectMapper.readValue(product.getImgUrls(), new TypeReference<List<String>>() {
            });
            imgs = imageUrls.stream().map(path -> {
                Map<String, String> map = new HashMap<>();
                map.put("path", path);
                map.put("url", minioService.getFileUrl(path));
                return map;
            }).collect(Collectors.toList());

        }
        productDetailResp.setImageUrls(imgs);

        //sku处理
        List<DetailSkuResp> skus = pItemsDao.selectList(new QueryWrapper<PItemsEntity>().eq("product_id", productId)).stream().map(sku -> {
            DetailSkuResp skuInfo = new DetailSkuResp();
            skuInfo.setSkuId(sku.getId());
            skuInfo.setStock(sku.getStock());
            skuInfo.setCostPrice(sku.getCostPrice());
            skuInfo.setSalePrice(sku.getSalePrice());
            Map<String, String> image = new HashMap<String, String>() {
                {
                    put("path", "");
                    put("url", "");
                }
            };
            if (StringUtils.isNotBlank(sku.getImg())) {
                image.put("path", sku.getImg());
                image.put("url", minioService.getFileUrl(sku.getImg()));
            }
            skuInfo.setImageObj(image);
            //spec 处理
            if (StringUtils.isNotBlank(sku.getSpecData())) {
                try {
                    skuInfo.setSpecList(objectMapper.readValue(sku.getSpecData(), new TypeReference<List<SpecInfo>>() {
                    }));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            return skuInfo;
        }).collect(Collectors.toList());
        productDetailResp.setSkus(skus);

        return productDetailResp;
    }

    @Override
    public List<PProductCategoryDto> categoryList() {
        List<PProductCategoryEntity> categoryList = pProductCategoryDao.selectList(new QueryWrapper<PProductCategoryEntity>().eq("`status`", 1));
        return categoryList.stream().map(PProductCategoryDto::new).collect(Collectors.toList());

    }

    @Override
    public List<PChinaRegionDto> areaData() {
        return pChinaRegionDao.selectList(new QueryWrapper<PChinaRegionEntity>()).stream().map(PChinaRegionDto::new).collect(Collectors.toList());
    }
}