package com.dk.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.coupon.entity.CouponEntity;

import java.util.Map;

/**
 * 优惠券信息
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 22:40:45
 */
public interface CouponService extends IService<CouponEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

