package com.dk.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;
import com.dk.gulimall.product.dao.BrandDao;
import com.dk.gulimall.product.dao.CategoryBrandRelationDao;
import com.dk.gulimall.product.dao.CategoryDao;
import com.dk.gulimall.product.entity.BrandEntity;
import com.dk.gulimall.product.entity.CategoryBrandRelationEntity;
import com.dk.gulimall.product.entity.CategoryEntity;
import com.dk.gulimall.product.service.BrandService;
import com.dk.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetails(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long categoryId = categoryBrandRelation.getCatelogId();

        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(categoryId);

        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        this.baseMapper.updateCategory(catId, name);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> brandRelationEntities = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_Id", catId));
        List<BrandEntity> brandEntities = brandRelationEntities.stream().map(item -> brandService.getById(item.getBrandId())).collect(Collectors.toList());
        return brandEntities;
    }

}