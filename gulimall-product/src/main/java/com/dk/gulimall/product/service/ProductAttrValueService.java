package com.dk.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dk.common.utils.PageUtils;
import com.dk.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 16:38:43
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void setProductAttr(List<ProductAttrValueEntity> attrValueEntities);

    List<ProductAttrValueEntity> baseAttrListForSpu(String spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}

