package com.yuuuuy.eshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderGoodsVO {
    Integer goodsId;
    String name;
    Integer amount;
    String imgPath;
    BigDecimal price;
    BigDecimal totalPrices;
}
