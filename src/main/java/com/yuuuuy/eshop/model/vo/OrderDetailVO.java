package com.yuuuuy.eshop.model.vo;

import com.yuuuuy.eshop.model.vo.GoodsVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVO {
    private Integer goodsId;
    private String name;
    private String imgPath;
    private Integer amount;
    private BigDecimal totalPrices;
    private BigDecimal price;
}
