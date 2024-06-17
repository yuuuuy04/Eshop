package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 修改用户参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    private Integer id;
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String password;
    private BigDecimal balance;
    private Character sex;
    private String avatar;
    private Character userType;
    private Character status;
}
