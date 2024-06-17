package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.AuthorityMapper;
import com.yuuuuy.eshop.model.entity.Authority;
import com.yuuuuy.eshop.service.AuthorityService;
import com.yuuuuy.eshop.service.RoleAuthorityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements AuthorityService {
    @Override
    public List<String> getAuthorities(List<Integer> authorityIds) {
        ArrayList<String> list = new ArrayList<>();
        for (Integer authorityId : authorityIds){
            LambdaQueryWrapper<Authority> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Authority::getId, authorityId)
                    .select(Authority::getName);
            Authority authority = baseMapper.selectOne(wrapper);
            list.add(authority.getName());
        }
        return list;
    }
}
