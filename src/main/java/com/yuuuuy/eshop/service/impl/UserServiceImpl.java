package com.yuuuuy.eshop.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.UserMapper;
import com.yuuuuy.eshop.model.dto.RegisterDTO;
import com.yuuuuy.eshop.model.dto.UpdateUserDTO;
import com.yuuuuy.eshop.model.dto.UserDTO;
import com.yuuuuy.eshop.model.entity.User;
import com.yuuuuy.eshop.model.params.LoginParam;
import com.yuuuuy.eshop.model.vo.TokenVO;
import com.yuuuuy.eshop.model.vo.admin.UserVO;
import com.yuuuuy.eshop.security.SecurityUser;
import com.yuuuuy.eshop.security.UserDetailsServiceImpl;
import com.yuuuuy.eshop.service.CartService;
import com.yuuuuy.eshop.service.UserService;
import com.yuuuuy.eshop.utils.JWTUtils;
import com.yuuuuy.eshop.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    CartService cartService;
    private final String AVATARPATH = System.getProperty("user.dir") + "\\static\\avatar\\";
    private final Integer TIME_OUT = 24;
    @Override
    public List<UserVO> getUserList(Integer page, Integer size) {
        Page<User> userPage = new Page<>();
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getDelFlag, "0")
                .select(User::getId, User::getUserName, User::getAvatar);
        List<UserVO> collect = baseMapper.selectPage(userPage, wrapper).getRecords()
                .stream()
                .map(entity -> {
                    UserVO userVO = new UserVO();
                    userVO.setId(entity.getId());
                    userVO.setUserName(entity.getUserName());
                    userVO.setAvatar(entity.getAvatar());
                    return userVO;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean addUser(MultipartFile multipartFile, UserDTO data) {
        //TODO 判断文件类型、大小
        String filename = multipartFile.getOriginalFilename();
        String uuid = IdUtil.fastSimpleUUID();
        String targetPath = AVATARPATH + uuid + filename;
        //处理异常
        try {
            FileUtil.writeBytes(multipartFile.getBytes(), targetPath);
        }catch (IOException e){
            throw new RuntimeException("添加用户失败");
        }
        //TODO 验证电话、邮箱格式

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        data.setPassword("{bcrypt}" + encoder.encode(data.getPassword()));
        User user = new User();
        user.fromUserDTO(data);
        user.setAvatar(uuid + filename);
        baseMapper.insert(user);
        //为用户创建一个购物车
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getId)
                .eq(User::getUserName, data.getUserName());
        User selectedOne = baseMapper.selectOne(wrapper);
        cartService.createCart(selectedOne.getId());
        return true;
    }

    @Override
    public Boolean updateUser(MultipartFile multipartFile, UpdateUserDTO data) {
        User user = new User();
        if(multipartFile != null){
            //TODO 判断文件类型和大小
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getId, data.getId())
                    .select(User::getAvatar);
            User selectOne = baseMapper.selectOne(wrapper);
            String oldPath = AVATARPATH + selectOne.getAvatar();
            FileUtil.del(new File(oldPath));
            String newPath = IdUtil.fastSimpleUUID() + multipartFile.getOriginalFilename();
            String targetPath = AVATARPATH + newPath;
            //TODO 处理异常
            try {
                FileUtil.writeBytes(multipartFile.getBytes(), targetPath);
            }catch (IOException e){
                throw new RuntimeException("更新头像失败");
            }
            user.setAvatar(newPath);
        }
        //TODO 验证电话、邮箱格式
        user.fromUUDTO(data);
        baseMapper.insert(user);
        return null;
    }

    @Override
    public TokenVO login(@NotNull LoginParam loginParam) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUserName(), loginParam.getPassword());
        //调用Manager的验证功能进行身份验证
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(authenticate == null){
            throw new RuntimeException("登录失败");
        }
        //获取用户信息, 生成JWT, 将用户信息存到redis, 返回JWT和用户权限
        SecurityUser user = (SecurityUser) authenticate.getPrincipal();
        Integer id = user.getId();
        String token = JWTUtils.createToken(id);
        redisUtils.setCacheObject("login:" + id, user, TIME_OUT, TimeUnit.HOURS);
        //返回头像地址
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, id)
                .select(User::getAvatar);
        User selectedOne = baseMapper.selectOne(wrapper);
        TokenVO tokenVO = new TokenVO(token, user.getUserType(), null);
        if(!Objects.isNull(selectedOne)){
            tokenVO.setAvatarPath(selectedOne.getAvatar());
        }
        return tokenVO;
    }

    @Override
    public Boolean logout(String token) {
        String id = JWTUtils.getID(token);
        redisUtils.deleteObject("login:" + id);
        return true;
    }

    @Override
    public Boolean register(RegisterDTO registerDTO) {
        //TODO 验证邮箱、密码格式
        //插入用户信息
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        registerDTO.setPassword("{bcrypt}" + encoder.encode(registerDTO.getPassword()));
        User user = new User();
        user.fromRegister(registerDTO);
        baseMapper.insert(user);
        //为用户创建一个购物车
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getId)
                .eq(User::getUserName, registerDTO.getUserName());
        User selectedOne = baseMapper.selectOne(wrapper);
        cartService.createCart(selectedOne.getId());
        return true;
    }

    @Override
    public BigDecimal getBalance(Integer userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(User::getBalance)
                .eq(User::getId, userId);
        User user = baseMapper.selectOne(wrapper);
        BigDecimal balance = user.getBalance();
        return balance;
    }

    @Override
    public Boolean payOrder(Integer userId, BigDecimal totalPrices) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, userId)
                .select(User::getBalance);
        User user = baseMapper.selectOne(wrapper);
        BigDecimal balance = user.getBalance().subtract(totalPrices);
        user.setBalance(balance);
        user.setId(userId);
        baseMapper.updateById(user);
        return true;
    }
}
