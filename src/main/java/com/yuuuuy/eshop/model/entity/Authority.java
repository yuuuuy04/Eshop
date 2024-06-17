package com.yuuuuy.eshop.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority extends BaseEntity{
    private Integer id;
    private String name;
}
