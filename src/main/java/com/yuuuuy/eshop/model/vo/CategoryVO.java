package com.yuuuuy.eshop.model.vo;

import com.yuuuuy.eshop.model.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private Integer id;
    private String name;

    public void fromEntity(Category category){
        id = category.getId();
        name = category.getName();
    }
}
