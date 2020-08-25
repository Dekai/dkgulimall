package com.dk.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.dk.gulimall.product.vo.AttrGroupRelationVo;
import com.dk.gulimall.product.vo.AttrRespVo;
import com.dk.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dk.gulimall.product.entity.AttrEntity;
import com.dk.gulimall.product.service.AttrService;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.R;



/**
 * 商品属性
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 17:51:45
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long cateLogId, @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params, cateLogId, attrType);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attr = attrService.getAttrInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
