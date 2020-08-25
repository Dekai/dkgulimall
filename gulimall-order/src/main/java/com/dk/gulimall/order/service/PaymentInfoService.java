package com.dk.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.order.entity.PaymentInfoEntity;

import java.util.Map;

/**
 * 支付信息表
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 23:09:40
 */
public interface PaymentInfoService extends IService<PaymentInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

