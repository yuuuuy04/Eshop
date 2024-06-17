package com.yuuuuy.eshop.controller.admin;

import com.yuuuuy.eshop.model.dto.UpdateUserDTO;
import com.yuuuuy.eshop.model.dto.UserDTO;
import com.yuuuuy.eshop.model.vo.admin.UserVO;
import com.yuuuuy.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('manage_user')")
@RequestMapping("admin/usermanager")
public class ManageUserController {
    @Autowired
    UserService userService;

    /**
     * 分页返回用户列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("list")
    public List<UserVO> getUserList(Integer page, Integer size){
        return userService.getUserList(page, size);
    }

    @PostMapping("add")
    public Boolean addUser(@RequestPart("avatar")MultipartFile multipartFile, @RequestBody UserDTO data){
        return userService.addUser(multipartFile, data);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("del")
    public Boolean delUser(Integer id){
        return userService.removeById(id);
    }

    /**
     * 更新用户信息
     * @param multipartFile
     * @param data
     * @return
     */
    @PostMapping("update")
    public Boolean updateUser(@RequestPart(value = "avatar", required = false)MultipartFile multipartFile, @RequestBody UpdateUserDTO data){
        return userService.updateUser(multipartFile, data);
    }
}
