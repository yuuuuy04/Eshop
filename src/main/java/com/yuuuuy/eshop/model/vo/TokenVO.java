package com.yuuuuy.eshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    private String JWT;
    private Character userType;
    private String avatarPath;
}
