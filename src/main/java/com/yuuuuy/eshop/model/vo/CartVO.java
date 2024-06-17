package com.yuuuuy.eshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVO {
    private List<CartGoodsVO> list;
    private BigDecimal totalPrices;
}
