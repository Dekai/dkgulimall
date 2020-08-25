package com.dk.gulimall.ware.dao;

import com.dk.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 23:13:11
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
