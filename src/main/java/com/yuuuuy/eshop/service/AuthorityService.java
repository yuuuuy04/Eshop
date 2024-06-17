package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.Authority;

import java.util.List;

public interface AuthorityService extends IService<Authority> {
    public List<String> getAuthorities(List<Integer> authorityIds);
}
