package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.RoleAuthorityMapper;
import com.yuuuuy.eshop.model.entity.RoleAuthority;
import com.yuuuuy.eshop.service.RoleAuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleAuthorityServiceImpl extends ServiceImpl<RoleAuthorityMapper, RoleAuthority> implements RoleAuthorityService {
    @Override
    public List<Integer> getAuthorityIds(Integer roleId) {
        LambdaQueryWrapper<RoleAuthority> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(RoleAuthority::getAuthorityId)
                .eq(RoleAuthority::getRoleId, roleId);
        List<Integer> collect = baseMapper.selectList(wrapper)
                .stream()
                .map(entity -> {
                    return entity.getAuthorityId();
                })
                .collect(Collectors.toList());
        return collect;
    }
}
