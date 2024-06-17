package com.yuuuuy.eshop.model.redisinfo;

import com.yuuuuy.eshop.model.dto.CartToOrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {
    private List<CartToOrderDTO> goodsInfo;
    private BigDecimal totalPrices;
}
