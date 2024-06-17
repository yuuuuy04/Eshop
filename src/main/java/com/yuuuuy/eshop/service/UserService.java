package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.dto.RegisterDTO;
import com.yuuuuy.eshop.model.dto.UpdateUserDTO;
import com.yuuuuy.eshop.model.dto.UserDTO;
import com.yuuuuy.eshop.model.entity.User;
import com.yuuuuy.eshop.model.params.LoginParam;
import com.yuuuuy.eshop.model.vo.TokenVO;
import com.yuuuuy.eshop.model.vo.admin.UserVO;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface UserService extends IService<User> {
    public List<UserVO> getUserList(Integer page, Integer size);
    public Boolean addUser(MultipartFile multipartFile, UserDTO data);
    public Boolean updateUser(MultipartFile multipartFile, UpdateUserDTO data);

    public TokenVO login(LoginParam loginParam);
    public Boolean register(RegisterDTO registerDTO);
    public BigDecimal getBalance(Integer userId);
    public Boolean payOrder(Integer userId, BigDecimal totalPrices);
    public Boolean logout(String token);
}
