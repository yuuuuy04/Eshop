package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartToOrderDTO {
    private Integer goodsId;
    private Integer amount;
    private BigDecimal totalPrices;
}
