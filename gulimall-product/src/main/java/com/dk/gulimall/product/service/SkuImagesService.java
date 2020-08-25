package com.dk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 16:38:43
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

