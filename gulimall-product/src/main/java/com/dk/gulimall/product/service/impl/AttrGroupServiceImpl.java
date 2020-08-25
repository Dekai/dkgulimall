package com.dk.gulimall.product.service.impl;

import com.dk.gulimall.product.entity.AttrEntity;
import com.dk.gulimall.product.service.AttrService;
import com.dk.gulimall.product.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;

import com.dk.gulimall.product.dao.AttrGroupDao;
import com.dk.gulimall.product.entity.AttrGroupEntity;
import com.dk.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long categoryId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((obj) ->
                    obj.eq("attr_group_id", key)
                            .or().like("attr_group_name", key)
            );
        }

        if (categoryId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), queryWrapper);
            return new PageUtils(page);
        } else {
            queryWrapper.eq("catelog_id", categoryId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catalogId) {
        List<AttrGroupEntity> groupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catalogId));

        List<AttrGroupWithAttrsVo> list = groupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo groupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, groupWithAttrsVo);

            List<AttrEntity> attrs = attrService.getRelationAttr(group.getAttrGroupId());
            groupWithAttrsVo.setAttrs(attrs);

            return groupWithAttrsVo;
        }).collect(Collectors.toList());

        return list;
    }
}