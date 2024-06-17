package com.yuuuuy.eshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuuuuy.eshop.model.entity.Category;
import com.yuuuuy.eshop.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService extends IService<Category> {
    public List<CategoryVO> getCategories();
    public Boolean addCategory(String name);
    public String getName(Integer id);
}
