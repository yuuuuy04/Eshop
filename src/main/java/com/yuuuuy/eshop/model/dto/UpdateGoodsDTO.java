package com.yuuuuy.eshop.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品修改参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoodsDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer SKU;
    private Integer categoryId;
}
