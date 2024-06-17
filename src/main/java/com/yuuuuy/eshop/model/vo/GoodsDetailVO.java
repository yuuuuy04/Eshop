package com.yuuuuy.eshop.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yuuuuy.eshop.model.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailVO {
    //加入购物车时需要
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    @TableField(value = "img_path")
    private String imgPath;
    private Integer sales;

    public void fromEntity(Goods goods){
        id = goods.getId();
        name = goods.getName();
        description = goods.getDescription();
        price = goods.getPrice();
        imgPath = goods.getImgPath();
        sales = goods.getSales();
    }
}
