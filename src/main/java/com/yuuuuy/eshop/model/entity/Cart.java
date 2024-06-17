package com.yuuuuy.eshop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private BigDecimal totalPrices;
    private Character status;
}
