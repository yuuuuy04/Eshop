package com.yuuuuy.eshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdUserDTO {
    private String nickName;
    private String phoneNumber;
    private Character sex;
}
