package com.dk.gulimall.product.dao;

import com.dk.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 16:38:44
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
