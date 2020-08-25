package com.dk.gulimall.order.dao;

import com.dk.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 23:09:40
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
