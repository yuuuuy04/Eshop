package com.yuuuuy.eshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuuuuy.eshop.model.entity.Category;
import com.yuuuuy.eshop.model.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
