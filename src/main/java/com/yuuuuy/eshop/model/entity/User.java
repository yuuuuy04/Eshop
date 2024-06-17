package com.yuuuuy.eshop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yuuuuy.eshop.model.dto.RegisterDTO;
import com.yuuuuy.eshop.model.dto.UpdateUserDTO;
import com.yuuuuy.eshop.model.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @TableId(type = IdType.AUTO)
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

    public void fromUserDTO(UserDTO data){
        this.userName = data.getUserName();
        this.nickName = data.getNickName();
        this.phoneNumber = data.getPhoneNumber();
        this.email = data.getEmail();
        this.password = data.getPassword();
        this.balance = data.getBalance();
        this.sex = data.getSex();
        this.userType = data.getUserType();
    }

    public void fromUUDTO(UpdateUserDTO data){
        this.id = data.getId();
        this.userName = data.getUserName();
        this.nickName = data.getNickName();
        this.phoneNumber = data.getPhoneNumber();
        this.email = data.getEmail();
        this.password = data.getPassword();
        this.balance = data.getBalance();
        this.sex = data.getSex();
        this.avatar = data.getAvatar();
        this.userType = data.getUserType();
        this.status = data.getStatus();
    }

    public void fromRegister(RegisterDTO data){
        this.userName = data.getUserName();
        this.nickName = data.getNickName();
        this.phoneNumber = data.getPhoneNumber();
        this.email = data.getEmail();
        this.password = data.getPassword();
    }
}
