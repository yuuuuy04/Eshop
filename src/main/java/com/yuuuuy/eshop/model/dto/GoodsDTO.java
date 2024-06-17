package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品添加参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private String name;
    private BigDecimal price;
    private Integer SKU;
    private String description;
    private Integer categoryId;
}
