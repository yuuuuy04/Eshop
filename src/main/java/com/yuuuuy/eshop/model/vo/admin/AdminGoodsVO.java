package com.yuuuuy.eshop.model.vo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yuuuuy.eshop.model.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminGoodsVO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer SKU;
    private String imgPath;
    private String category;
    private Character status;

    public void transfer(Goods goods){
        this.name = goods.getName();
        this.description = goods.getDescription();
        this.price = goods.getPrice();
        this.SKU = goods.getSKU();
        //额外查询category
        this.status = goods.getStatus();
        this.imgPath = goods.getImgPath();
        this.id = goods.getId();
    }
}
