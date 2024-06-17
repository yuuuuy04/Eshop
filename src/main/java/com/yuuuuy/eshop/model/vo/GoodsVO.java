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
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVO {
    //打开详细页时需要id
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private BigDecimal price;
    @TableField(value = "img_path")
    private String imgPath;
    public void fromEntity(Goods goods){
        id = goods.getId();
        name = goods.getName();
        price = goods.getPrice();
        imgPath = goods.getImgPath();
    }
}
