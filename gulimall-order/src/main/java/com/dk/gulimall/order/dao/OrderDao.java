package com.dk.gulimall.order.dao;

import com.dk.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 23:09:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
