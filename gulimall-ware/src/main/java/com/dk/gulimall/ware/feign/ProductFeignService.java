package com.dk.gulimall.ware.feign;

import com.dk.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    @RequestMapping("/product/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
