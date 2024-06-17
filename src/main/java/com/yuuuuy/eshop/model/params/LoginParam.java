package com.yuuuuy.eshop.model.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam implements Serializable {
    private String userName;
    private String password;
}
