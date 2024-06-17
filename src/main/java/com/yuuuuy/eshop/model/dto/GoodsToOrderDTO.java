package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询订单中商品的信息, 传到订单服务
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsToOrderDTO {
    private String imgPath;
    private String name;
}
