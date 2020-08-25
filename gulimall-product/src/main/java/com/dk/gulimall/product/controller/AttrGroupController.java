package com.dk.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.dk.gulimall.product.entity.AttrEntity;
import com.dk.gulimall.product.service.AttrAttrgroupRelationService;
import com.dk.gulimall.product.service.AttrService;
import com.dk.gulimall.product.service.CategoryService;
import com.dk.gulimall.product.vo.AttrGroupRelationVo;
import com.dk.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.dk.gulimall.product.entity.AttrGroupEntity;
import com.dk.gulimall.product.service.AttrGroupService;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.R;


/**
 * 属性分组
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 17:51:45
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;
    /**
     * 列表
     */
    @RequestMapping("/list/{categoryId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("categoryId") Long categoryId) {
        PageUtils page = attrGroupService.queryPage(params, categoryId);

        return R.ok().put("page", page);
    }

    ///product/attrgroup/{catelogId}/withattr
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catalogId) {
       List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catalogId);
        return R.ok().put("data", vos);
    }

    @GetMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<AttrEntity> list = attrService.getRelationAttr(attrGroupId);

        return R.ok().put("data", list);
    }

    ///product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R noAttrRelation(@PathVariable("attrGroupId") Long attrGroupId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(attrGroupId, params);

        return R.ok().put("page", page);
    }

    //product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos) {
        relationService.saveBatch(vos);

        return R.ok();
    }


    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos) {
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long categoryId = attrGroup.getCatelogId();
        Long[] categoryPath = categoryService.findCategoryPath(categoryId);
        attrGroup.setCatelogPath(categoryPath);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
