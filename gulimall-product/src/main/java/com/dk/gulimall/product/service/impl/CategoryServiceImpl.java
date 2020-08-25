package com.dk.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;
import com.dk.gulimall.product.dao.CategoryDao;
import com.dk.gulimall.product.entity.CategoryEntity;
import com.dk.gulimall.product.service.CategoryBrandRelationService;
import com.dk.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);

        return entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).peek(menu ->
                menu.setChildren(getChildren(menu, entities))
        ).sorted(
                Comparator.comparingInt(CategoryEntity::getSort)
        ).collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO: Check if there is any reference
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCategoryPath(Long categoryId) {
        List<Long> paths = new ArrayList<>();
        findParentPath(categoryId, paths);

        Collections.reverse(paths);

        return paths.toArray(new Long[paths.size()]);
    }

    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    private List<Long> findParentPath(Long categoryId, List<Long> paths) {
        paths.add(categoryId);
        CategoryEntity categoryEntity = this.getById(categoryId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }
        return paths;
    }

    private List<CategoryEntity> getChildren(CategoryEntity parentCategory, List<CategoryEntity> categoryEntities) {

        return categoryEntities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid().equals(parentCategory.getCatId())
        ).peek(menu ->
                menu.setChildren(getChildren(menu, categoryEntities))
        ).sorted(
                Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))
        ).collect(Collectors.toList());
    }


}