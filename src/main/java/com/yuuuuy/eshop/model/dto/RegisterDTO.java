package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private String userName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String password;
}
