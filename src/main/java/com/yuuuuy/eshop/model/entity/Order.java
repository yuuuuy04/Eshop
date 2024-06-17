package com.yuuuuy.eshop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "e_order")
public class Order extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer number;
    private Integer userId;
    private BigDecimal totalPrices;
    private Character status;
}
