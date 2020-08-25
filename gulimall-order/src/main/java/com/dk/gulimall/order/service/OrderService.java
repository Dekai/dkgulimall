package com.dk.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 23:09:39
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

