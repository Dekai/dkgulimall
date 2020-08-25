package com.dk.gulimall.product.service.impl;

import com.dk.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;

import com.dk.gulimall.product.dao.BrandDao;
import com.dk.gulimall.product.entity.BrandEntity;
import com.dk.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> objectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            objectQueryWrapper.eq("brand_id", key).or().like("name", key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                objectQueryWrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateDetails(BrandEntity brand) {
        //Keep related data consistent
        this.updateById(brand);

        if(!StringUtils.isEmpty(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
        }
    }

}