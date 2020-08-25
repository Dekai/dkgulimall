package com.dk.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dk.gulimall.product.entity.BrandEntity;
import com.dk.gulimall.product.service.BrandService;
import com.dk.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;


@Slf4j
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Test
    public void testFindParentPath() {
        Long[] paths = categoryService.findCategoryPath(225L);
        log.info("Complete path: {}", Arrays.asList(paths));
    }

//    @Test
    void contextLoads() {

        QueryWrapper<BrandEntity> brandEntityQueryWrapper = new QueryWrapper<>();
        brandEntityQueryWrapper.eq("name", "Apple");

        BrandEntity brandEntity = brandService.getOne(brandEntityQueryWrapper);

        brandEntity.setName("Apple");

        brandService.updateById(brandEntity);

        System.out.println("Updated successfully!");
    }

}
