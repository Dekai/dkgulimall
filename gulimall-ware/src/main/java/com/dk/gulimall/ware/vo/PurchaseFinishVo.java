package com.dk.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PurchaseFinishVo {
    @NotNull
    private Long id;
}
