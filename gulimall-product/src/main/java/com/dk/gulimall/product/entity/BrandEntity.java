package com.dk.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dk.common.valid.AddGroup;
import com.dk.common.valid.ListValue;
import com.dk.common.valid.UpdateGroup;
import com.dk.common.valid.UpdateStatusGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author Dekai
 * @email dekai@gmail.com
 * @date 2020-08-01 16:38:43
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @NotNull(message = "Update require Id", groups = {UpdateGroup.class, UpdateStatusGroup.class})
    @Null(message = "Add can not have Id", groups = {AddGroup.class})
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "Brand Name is mandatory!", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotBlank(groups = {AddGroup.class})
    @URL(message = "Must be a URL", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     */
    @NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
    @ListValue(vals = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    @NotEmpty(groups = {AddGroup.class})
    @Pattern(regexp = "^[a-zA-Z]$", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = {AddGroup.class})
    @Min(value = 0, groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
