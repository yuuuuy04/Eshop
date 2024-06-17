package com.yuuuuy.eshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoVO {
    Integer orderId;
    List<OrderGoodsVO> orderGoodsVOS;
    BigDecimal totalPrices;
}
