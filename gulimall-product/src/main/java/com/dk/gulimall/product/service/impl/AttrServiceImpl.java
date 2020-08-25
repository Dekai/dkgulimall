package com.dk.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.constant.ProductConstant;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;
import com.dk.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.dk.gulimall.product.dao.AttrDao;
import com.dk.gulimall.product.dao.AttrGroupDao;
import com.dk.gulimall.product.dao.CategoryDao;
import com.dk.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.dk.gulimall.product.entity.AttrEntity;
import com.dk.gulimall.product.entity.AttrGroupEntity;
import com.dk.gulimall.product.entity.CategoryEntity;
import com.dk.gulimall.product.service.AttrService;
import com.dk.gulimall.product.service.CategoryService;
import com.dk.gulimall.product.vo.AttrGroupRelationVo;
import com.dk.gulimall.product.vo.AttrRespVo;
import com.dk.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);

        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());

            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long cateLogId, String attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type",
                "base".equalsIgnoreCase(attrType) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (cateLogId != 0) {
            queryWrapper.eq("catelog_id", cateLogId);
        }

        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) ->
                    wrapper.eq("attr_id", key).or().like("attr_name", key)
            );
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrRespVo> respVos = page.getRecords().stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            if ("base".equalsIgnoreCase(attrType)) {
                AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());

        pageUtils.setList(respVos);

        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (relationEntity != null) {
                attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }

        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        attrRespVo.setCatelogPath(categoryService.findCategoryPath(attrEntity.getCatelogId()));

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_Id", attr.getAttrId()));
            if (count > 0) {
                attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_Id", attr.getAttrId()));
            } else {
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }
        }
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));

        if (entities != null && entities.size() > 0) {
            List<Long> list = entities.stream().map((attrAttrgroupRelationEntity -> {
                return attrAttrgroupRelationEntity.getAttrId();
            })).collect(Collectors.toList());
            return this.listByIds(list);
        } else {
            return null;
        }
    }

    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchRelation(entities);
    }

    @Override
    public PageUtils getNoRelationAttr(Long attrGroupId, Map<String, Object> params) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long categoryId = attrGroupEntity.getCatelogId();

        List<AttrGroupEntity> groupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", categoryId));
        List<Long> groupIds = groupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());

        QueryWrapper<AttrAttrgroupRelationEntity> relationEntityQueryWrapper = new QueryWrapper<>();

        if(groupIds.size() > 0) {
            relationEntityQueryWrapper.in("attr_group_id", groupIds);
        }
        List<AttrAttrgroupRelationEntity> attr_group_id = attrAttrgroupRelationDao.selectList(relationEntityQueryWrapper);

        List<Long> attrIds = attr_group_id.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", categoryId).eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds.size() > 0) {
            queryWrapper.notIn("attr_id", attrIds);
        }


        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(wrapper -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);

        return new PageUtils(page);
    }
}