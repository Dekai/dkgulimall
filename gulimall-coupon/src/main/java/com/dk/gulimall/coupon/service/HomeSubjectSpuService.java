package com.dk.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * 专题商品
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 22:40:45
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

