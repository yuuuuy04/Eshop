package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.RoleAuthority;

import java.util.List;

public interface RoleAuthorityService extends IService<RoleAuthority> {
    public List<Integer> getAuthorityIds(Integer roleId);
}
