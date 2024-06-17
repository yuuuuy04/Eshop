package com.yuuuuy.eshop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartGoods extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer cartId;
    private Integer goodsId;
    private Integer amount;
    private BigDecimal totalPrices;
}
