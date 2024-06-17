package com.yuuuuy.eshop.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartGoodsVO {
    //cart_goods id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //商品id
    private Integer goodsId;
    private BigDecimal price;
    private BigDecimal totalPrices;
    private Integer amount;
    private String name;
    private String imgPath;
}
