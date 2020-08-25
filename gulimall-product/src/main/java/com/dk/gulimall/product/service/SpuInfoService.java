package com.dk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.product.entity.SkuInfoEntity;
import com.dk.gulimall.product.entity.SpuInfoDescEntity;
import com.dk.gulimall.product.entity.SpuInfoEntity;
import com.dk.gulimall.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 16:38:43
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

}

