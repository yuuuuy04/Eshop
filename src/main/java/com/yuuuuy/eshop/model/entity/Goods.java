package com.yuuuuy.eshop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yuuuuy.eshop.model.dto.GoodsDTO;
import com.yuuuuy.eshop.model.dto.UpdateGoodsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imgPath;
    @TableField(value = "SKU")
    private Integer SKU;
    private Integer categoryId;
    private Integer sales;
    private Character status;

    public void fromDTO(GoodsDTO goodsDTO){
        name = goodsDTO.getName();
        price = goodsDTO.getPrice();
        SKU = goodsDTO.getSKU();
        description = goodsDTO.getDescription();
        categoryId = goodsDTO.getCategoryId();
    }

    public void fromUGDTO(UpdateGoodsDTO data){
        this.id = data.getId();
        this.name = data.getName();
        this.description = data.getDescription();
        this.price = data.getPrice();
        this.SKU = data.getSKU();
        this.categoryId = data.getCategoryId();
    }
}
