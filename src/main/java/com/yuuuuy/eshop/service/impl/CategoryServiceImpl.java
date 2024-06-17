package com.yuuuuy.eshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuuuuy.eshop.dao.CategoryMapper;
import com.yuuuuy.eshop.model.entity.Category;
import com.yuuuuy.eshop.model.vo.CategoryVO;
import com.yuuuuy.eshop.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public List<CategoryVO> getCategories(){
        List<CategoryVO> collect = baseMapper.selectList(
                        Wrappers.<Category>lambdaQuery()
                                .eq(Category::getDelFlag, 0)
                                .select(Category::getId, Category::getName)
                )
                .stream()
                .map(entity -> {
                    CategoryVO categoryVO = new CategoryVO();
                    categoryVO.fromEntity(entity);
                    return categoryVO;
                })
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Boolean addCategory(String name) {
        Category category = new Category();
        //TODO 处理DuplicateKeyException分类重名错误
        category.setName(name);
        return (baseMapper.insert(category) == 1);
    }

    @Override
    public String getName(Integer id) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getId, id)
                .select(Category::getName);
        Category category = baseMapper.selectOne(wrapper);
        return category.getName();
    }
}
