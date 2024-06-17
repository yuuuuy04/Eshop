package com.yuuuuy.eshop.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuuuuy.eshop.dao.UserMapper;
import com.yuuuuy.eshop.model.entity.User;
import com.yuuuuy.eshop.service.AuthorityService;
import com.yuuuuy.eshop.service.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsManager {
    @Autowired
    UserMapper userMapper;
    @Resource
    RoleAuthorityService roleAuthorityService;
    @Resource
    AuthorityService authorityService;
    @Override
    public void createUser(UserDetails user) {
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, username)
                        .select(User::getId, User::getPassword, User::getUserType);
        User user = userMapper.selectOne(wrapper);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        //查用户权限
        List<Integer> authorityIds = roleAuthorityService.getAuthorityIds(Integer.parseInt(user.getUserType().toString()));
        List<String> authorities = authorityService.getAuthorities(authorityIds);
        SecurityUser loginUser = new SecurityUser();
        loginUser.setId(user.getId());
        loginUser.setUserName(username);
        loginUser.setPassword(user.getPassword());
        loginUser.setUserType(user.getUserType());
        loginUser.setPermissions(authorities);
        return loginUser;
    }
}
