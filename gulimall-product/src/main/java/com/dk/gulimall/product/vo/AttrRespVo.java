package com.dk.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo {
    private String CatelogName;
    private String groupName;
    private Long[] catelogPath;
}
