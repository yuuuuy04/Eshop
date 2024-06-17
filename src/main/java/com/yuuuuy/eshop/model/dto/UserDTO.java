package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 添加用户参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String password;
    private BigDecimal balance;
    private Character sex;
    private Character userType;
}
