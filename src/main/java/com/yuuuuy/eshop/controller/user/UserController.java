package com.yuuuuy.eshop.controller.user;

import com.yuuuuy.eshop.model.vo.UserInfoVO;
import com.yuuuuy.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/common")
public class UserController {
    @Autowired
    UserService userService;

    public Boolean updateUserInfo(@RequestHeader("token") String token){
        return true;
    }
    public Boolean updateAvatar(@RequestHeader("token") String token, @RequestPart(value = "img") MultipartFile multipartFile){
        return null;
    }
    @PostMapping("updpswd")
    public Boolean updatePassword(@RequestHeader("token") String token, String prePswd, String newPswd){
        
        return true;
    }

    @PostMapping("logout")
    public Boolean logout(@RequestHeader("token") String token){
        return userService.logout(token);
    }
    public UserInfoVO getUserInfo(@RequestHeader("token") String token){
        return null;
    }
}
