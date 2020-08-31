package com.dk.gulimall.ware.service.impl;

import com.dk.common.constant.WareConstant;
import com.dk.gulimall.ware.entity.PurchaseDetailEntity;
import com.dk.gulimall.ware.service.PurchaseDetailService;
import com.dk.gulimall.ware.service.WareSkuService;
import com.dk.gulimall.ware.vo.MergeVo;
import com.dk.gulimall.ware.vo.PurchaseDoneVo;
import com.dk.gulimall.ware.vo.PurchaseItemDoneVo;
import org.omg.CORBA.WCharSeqHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dk.common.utils.PageUtils;
import com.dk.common.utils.Query;

import com.dk.gulimall.ware.dao.PurchaseDao;
import com.dk.gulimall.ware.entity.PurchaseEntity;
import com.dk.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService detailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void done(PurchaseDoneVo purchaseDoneVo) {
        //Update purchase order status
        Long purchaseOrderId = purchaseDoneVo.getId();

        //Update purchase item status
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        List<PurchaseDetailEntity> updateDItems = new ArrayList<>();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();

            detailEntity.setId(item.getItemId());
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.ERROR.getCode()) {
                flag = false;
                detailEntity.setStatus(item.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISHED.getCode());
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                wareSkuService.addStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }

            updateDItems.add(detailEntity);
        }

        detailService.updateBatchById(updateDItems);

        //update purchase status

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseOrderId);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISHED.getCode() : WareConstant.PurchaseStatusEnum.ERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());

        this.updateById(purchaseEntity);
    }

    @Override
    public void received(List<Long> ids) {
        // purchase order status
        List<PurchaseEntity> purchaseEntities = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //update purchase order status
        this.updateBatchById(purchaseEntities);

        //update purchase request stats
        purchaseEntities.forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetails = detailService.listDetailByPurchaseId(purchaseEntity.getId());
            List<PurchaseDetailEntity> detailEntities = purchaseDetails.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());

            detailService.updateBatchById(detailEntities);
        });
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();

        if (purchaseId == null || this.getById(purchaseId) == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();

            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(0);
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        PurchaseEntity entity = this.getById(purchaseId);
        if (entity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() || entity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
            List<Long> items = mergeVo.getItems();
            Long finalPurchaseId = purchaseId;
            List<PurchaseDetailEntity> collect = items.stream().map(itemId -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();

                purchaseDetailEntity.setId(itemId);
                purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());

                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            detailService.updateBatchById(collect);

            PurchaseEntity updatedPurchase = new PurchaseEntity();
            updatedPurchase.setId(purchaseId);
            updatedPurchase.setUpdateTime(new Date());
            this.updateById(updatedPurchase);
        } else {
            log.error("Invalid Purchase order status");
        }
    }
}