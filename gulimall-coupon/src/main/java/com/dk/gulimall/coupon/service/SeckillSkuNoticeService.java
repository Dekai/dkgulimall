package com.dk.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 22:40:45
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

