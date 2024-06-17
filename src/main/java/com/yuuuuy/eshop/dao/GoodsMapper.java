package com.yuuuuy.eshop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuuuuy.eshop.model.dto.GoodsToOrderDTO;
import com.yuuuuy.eshop.model.entity.Goods;
import com.yuuuuy.eshop.model.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
